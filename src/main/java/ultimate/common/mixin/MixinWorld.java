package ultimate.common.mixin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.ITickable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import ultimate.UltimateMod;
import ultimate.common.util.UltimateEntityList;
import ultimate.common.util.UltimateUtil;

@Mixin({ World.class })
public abstract class MixinWorld {
    private World world = (World) (Object) this;

    @Shadow
    @Final
    @Mutable
    private List<Entity> loadedEntityList;

    @Shadow
    @Final
    private Profiler profiler;

    @Shadow
    @Final
    private List<Entity> weatherEffects;

    @Shadow
    @Final
    private List<Entity> unloadedEntityList;

    @Shadow
    @Final
    private List<TileEntity> tileEntitiesToBeRemoved;

    @Shadow
    @Final
    private List<TileEntity> tickableTileEntities;

    @Shadow
    @Final
    private List<TileEntity> loadedTileEntityList;

    @Shadow
    @Final
    private WorldBorder worldBorder;

    @Shadow
    @Final
    private List<TileEntity> addedTileEntityList;

    @Shadow
    private boolean processingLoadedTiles;

    @Shadow
    private List<IWorldEventListener> eventListeners;

    @Shadow
    protected abstract void removeEntity(Entity entityIn);

    @Shadow
    protected abstract Chunk getChunkFromChunkCoords(int chunkX, int chunkZ);

    @Shadow
    protected abstract boolean isChunkLoaded(int x, int z, boolean allowEmpty);

    @Shadow
    protected abstract void shadow$onEntityRemoved(Entity entityIn);

    @Shadow
    protected abstract void tickPlayers();

    @Shadow
    protected abstract void updateEntity(Entity ent);

    @Shadow
    protected abstract boolean isBlockLoaded(BlockPos pos);

    @Shadow
    protected abstract boolean isBlockLoaded(BlockPos pos, boolean allowEmpty);

    @Shadow
    protected abstract void removeTileEntity(BlockPos pos);

    @Shadow
    protected abstract Chunk getChunkFromBlockCoords(BlockPos pos);

    @Shadow
    protected abstract void notifyBlockUpdate(BlockPos pos, IBlockState oldState, IBlockState newState, int flags);

    @Shadow
    protected abstract boolean addTileEntity(TileEntity tile);

    private static final Field LOADED_ENTITY_LIST = ObfuscationReflectionHelper.findField(World.class,
            "field_72996_f");
    static {
        LOADED_ENTITY_LIST.setAccessible(true);
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(LOADED_ENTITY_LIST, LOADED_ENTITY_LIST.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            UltimateMod.getLogger().error("Error initializing WorldMixin.", e);
        }
    }

    @Inject(method = "removeEntity", at = @At("HEAD"), cancellable = true)
    public void removeEntity(Entity entityIn, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityIn)) {
            info.cancel();
        }
    }

    @Inject(method = "removeEntityDangerously", at = @At("HEAD"), cancellable = true)
    public void removeEntityDangerously(Entity entityIn, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityIn)) {
            info.cancel();
        }
    }

    @Inject(method = "onEntityRemoved", at = @At("HEAD"), cancellable = true)
    public void onEntityRemoved(Entity entityIn, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityIn)) {
            info.cancel();
        }
    }

    /**
     * @author Minsk
     * @reason
     *         change world.loadedEntityList to UltimateEntityList
     *         ignore entity.isDead if the entity is ultimate player
     */
    @Overwrite
    public void updateEntities() {
        if (!(loadedEntityList instanceof UltimateEntityList)) {
            try {
                LOADED_ENTITY_LIST.set(world, new UltimateEntityList(loadedEntityList));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                UltimateMod.getLogger().error("Error initializing WorldMixin.", e);
                return;
            }
        }
        this.profiler.startSection("entities");
        this.profiler.startSection("global");

        for (int i = 0; i < this.weatherEffects.size(); ++i) {
            Entity entity = this.weatherEffects.get(i);

            try {
                if (entity.updateBlocked)
                    continue;
                ++entity.ticksExisted;
                entity.onUpdate();
            } catch (Throwable throwable2) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");

                // Ultimate - Fix dead code
                entity.addEntityCrashInfo(crashreportcategory);

                if (net.minecraftforge.common.ForgeModContainer.removeErroringEntities) {
                    net.minecraftforge.fml.common.FMLLog.log.fatal("{}", crashreport.getCompleteReport());
                    removeEntity(entity);
                } else
                    throw new ReportedException(crashreport);
            }

            if (entity.isDead) {
                this.weatherEffects.remove(i--);
            }
        }

        this.profiler.endStartSection("remove");

        unloadedEntityList.removeIf(UltimateUtil::isUltimatePlayer);
        this.loadedEntityList.removeAll(this.unloadedEntityList);

        for (int k = 0; k < this.unloadedEntityList.size(); ++k) {
            Entity entity1 = this.unloadedEntityList.get(k);
            if (UltimateUtil.isUltimatePlayer(entity1)) {
                continue;
            }

            int j = entity1.chunkCoordX;
            int k1 = entity1.chunkCoordZ;

            if (entity1.addedToChunk && this.isChunkLoaded(j, k1, true)) {
                this.getChunkFromChunkCoords(j, k1).removeEntity(entity1);
            }
        }

        for (int l = 0; l < this.unloadedEntityList.size(); ++l) {
            Entity entity1 = this.unloadedEntityList.get(l);
            if (UltimateUtil.isUltimatePlayer(entity1)) {
                continue;
            }
            this.shadow$onEntityRemoved(entity1);
        }

        this.unloadedEntityList.clear();
        this.tickPlayers();
        this.profiler.endStartSection("regular");

        for (int i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
            Entity entity2 = this.loadedEntityList.get(i1);
            if (UltimateUtil.isUltimatePlayer(entity2)) {
                entity2.isDead = false;
            }

            Entity entity3 = entity2.getRidingEntity();

            if (entity3 != null) {
                if (!entity3.isDead && entity3.isPassenger(entity2)) {
                    continue;
                }

                entity2.dismountRidingEntity();
            }

            this.profiler.startSection("tick");

            if (UltimateUtil.isUltimatePlayer(entity2) || (!entity2.isDead && !(entity2 instanceof EntityPlayerMP))) {
                try {
                    net.minecraftforge.server.timings.TimeTracker.ENTITY_UPDATE.trackStart(entity2);
                    this.updateEntity(entity2);
                    net.minecraftforge.server.timings.TimeTracker.ENTITY_UPDATE.trackEnd(entity2);
                } catch (Throwable throwable1) {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking entity");
                    CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Entity being ticked");
                    entity2.addEntityCrashInfo(crashreportcategory1);
                    if (net.minecraftforge.common.ForgeModContainer.removeErroringEntities) {
                        net.minecraftforge.fml.common.FMLLog.log.fatal("{}", crashreport1.getCompleteReport());
                        removeEntity(entity2);
                    } else
                        throw new ReportedException(crashreport1);
                }
            }

            this.profiler.endSection();
            this.profiler.startSection("remove");

            if (!UltimateUtil.isUltimatePlayer(entity2) && (entity2.isDead
                    || UltimateUtil.getUltimateDeathTime(entity3) >= 20)) {
                int l1 = entity2.chunkCoordX;
                int i2 = entity2.chunkCoordZ;

                if (entity2.addedToChunk && this.isChunkLoaded(l1, i2, true)) {
                    this.getChunkFromChunkCoords(l1, i2).removeEntity(entity2);
                }

                this.loadedEntityList.remove(i1--);
                this.shadow$onEntityRemoved(entity2);
            }

            this.profiler.endSection();
        }

        this.profiler.endStartSection("blockEntities");

        this.processingLoadedTiles = true; // FML Move above remove to prevent CMEs

        if (!this.tileEntitiesToBeRemoved.isEmpty()) {
            for (Object tile : tileEntitiesToBeRemoved) {
                ((TileEntity) tile).onChunkUnload();
            }

            // forge: faster "contains" makes this removal much more efficient
            java.util.Set<TileEntity> remove = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
            remove.addAll(tileEntitiesToBeRemoved);
            this.tickableTileEntities.removeAll(remove);
            this.loadedTileEntityList.removeAll(remove);
            this.tileEntitiesToBeRemoved.clear();
        }

        Iterator<TileEntity> iterator = this.tickableTileEntities.iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = iterator.next();

            if (!tileentity.isInvalid() && tileentity.hasWorld()) {
                BlockPos blockpos = tileentity.getPos();

                if (this.isBlockLoaded(blockpos, false) && this.worldBorder.contains(blockpos)) // Forge: Fix TE's
                                                                                                // getting an extra tick
                                                                                                // on the client
                                                                                                // side....
                {
                    try {
                        this.profiler.func_194340_a(() -> {
                            return String.valueOf((Object) TileEntity.getKey(tileentity.getClass()));
                        });
                        net.minecraftforge.server.timings.TimeTracker.TILE_ENTITY_UPDATE.trackStart(tileentity);
                        ((ITickable) tileentity).update();
                        net.minecraftforge.server.timings.TimeTracker.TILE_ENTITY_UPDATE.trackEnd(tileentity);
                        this.profiler.endSection();
                    } catch (Throwable throwable) {
                        CrashReport crashreport2 = CrashReport.makeCrashReport(throwable, "Ticking block entity");
                        CrashReportCategory crashreportcategory2 = crashreport2
                                .makeCategory("Block entity being ticked");
                        tileentity.addInfoToCrashReport(crashreportcategory2);
                        if (net.minecraftforge.common.ForgeModContainer.removeErroringTileEntities) {
                            net.minecraftforge.fml.common.FMLLog.log.fatal("{}", crashreport2.getCompleteReport());
                            tileentity.invalidate();
                            this.removeTileEntity(tileentity.getPos());
                        } else
                            throw new ReportedException(crashreport2);
                    }
                }
            }

            if (tileentity.isInvalid()) {
                iterator.remove();
                this.loadedTileEntityList.remove(tileentity);

                if (this.isBlockLoaded(tileentity.getPos())) {
                    // Forge: Bugfix: If we set the tile entity it immediately sets it in the chunk,
                    // so we could be desyned
                    Chunk chunk = this.getChunkFromBlockCoords(tileentity.getPos());
                    if (chunk.getTileEntity(tileentity.getPos(),
                            net.minecraft.world.chunk.Chunk.EnumCreateEntityType.CHECK) == tileentity)
                        chunk.removeTileEntity(tileentity.getPos());
                }
            }
        }

        this.processingLoadedTiles = false;
        this.profiler.endStartSection("pendingBlockEntities");

        if (!this.addedTileEntityList.isEmpty()) {
            for (int j1 = 0; j1 < this.addedTileEntityList.size(); ++j1) {
                TileEntity tileentity1 = this.addedTileEntityList.get(j1);

                if (!tileentity1.isInvalid()) {
                    if (!this.loadedTileEntityList.contains(tileentity1)) {
                        this.addTileEntity(tileentity1);
                    }

                    if (this.isBlockLoaded(tileentity1.getPos())) {
                        Chunk chunk = this.getChunkFromBlockCoords(tileentity1.getPos());
                        IBlockState iblockstate = chunk.getBlockState(tileentity1.getPos());
                        chunk.addTileEntity(tileentity1.getPos(), tileentity1);
                        this.notifyBlockUpdate(tileentity1.getPos(), iblockstate, iblockstate, 3);
                    }
                }
            }

            this.addedTileEntityList.clear();
        }

        this.profiler.endSection();
        this.profiler.endSection();
    }

    /**
     * @author Minsk
     * @reason Bypass warning
     */
    @Overwrite
    public void onEntityRemoved(Entity entityIn) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            ((IWorldEventListener) this.eventListeners.get(i)).onEntityRemoved(entityIn);
        }

        if (UltimateUtil.isUltimateDead(entityIn)) {
            try {
                Field field = Entity.class.getDeclaredField("isAddedToWorld");
                field.setAccessible(true);
                field.set(entityIn, true);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                UltimateMod.getLogger().error("Error removing entity", e);
            }
        } else {
            entityIn.onRemovedFromWorld();
        }
    }
}
