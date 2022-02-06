package ultimate.common.mixin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import ultimate.UltimateMod;
import ultimate.common.core.interfaces.IMixinWorld;
import ultimate.common.util.UltimateEntityList;
import ultimate.common.util.UltimateUtil;

@Mixin({ World.class })
public abstract class MixinWorld implements IMixinWorld {
    private World world = (World) (Object) this;
    private boolean theWorld;

    @Shadow
    @Final
    @Mutable
    private List<Entity> loadedEntityList;

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

    @Inject(method = "updateEntities", at = @At("HEAD"), cancellable = true)
    public void updateEntities(CallbackInfo info) throws IllegalArgumentException, IllegalAccessException {
        if (!(loadedEntityList instanceof UltimateEntityList)) {
            LOADED_ENTITY_LIST.set(world, new UltimateEntityList(loadedEntityList));
        }

        Iterator<Entity> iterator = loadedEntityList.iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();
            if (UltimateUtil.isUltimatePlayer(e)) {
                e.isDead = false;

                if (theWorld) {
                    updateEntity(e);
                }
            } else if (UltimateUtil.isUltimateDead(e)) {
                if (e instanceof EntityLivingBase) {
                    int tick = UltimateUtil.increaseUltimateDeathTime(e);
                    if (tick >= 20) {
                        fastRemove(e);
                        iterator.remove();

                        // Particle Effects
                        Random rand = new Random();
                        for (int k = 0; k < 20; ++k) {
                            double d2 = rand.nextGaussian() * 0.02D;
                            double d0 = rand.nextGaussian() * 0.02D;
                            double d1 = rand.nextGaussian() * 0.02D;
                            e.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
                                    e.posX + (double) (rand.nextFloat() * e.width * 2.0F)
                                            - (double) e.width,
                                    e.posY + (double) (rand.nextFloat() * e.height),
                                    e.posZ + (double) (rand.nextFloat() * e.width * 2.0F)
                                            - (double) e.width,
                                    d2, d0, d1);
                        }
                    }
                } else {
                    fastRemove(e);
                    iterator.remove();
                }
            }
        }

        if (theWorld) {
            info.cancel();
        }
    }

    @Redirect(method = "updateEntities", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;isDead:Z"))
    public boolean redirect$isDead$updateEntities(Entity e) {
        if (UltimateUtil.isUltimatePlayer(e)) {
            return false;
        }

        if (UltimateUtil.isUltimateDead(e)) {
            if (e instanceof EntityLivingBase) {
                if (UltimateUtil.getUltimateDeathTime(e) >= 20) {
                    return true;
                }

                return e.isDead;
            } else {
                return true;
            }
        }

        return e.isDead;
    }

    /**
     * @author Minsk
     * @reason Bypass warning
     */
    @Overwrite
    public void onEntityRemoved(Entity entityIn) {
        if (UltimateUtil.isUltimatePlayer(entityIn)) {
            return;
        }

        for (int i = 0; i < eventListeners.size(); ++i) {
            ((IWorldEventListener) eventListeners.get(i)).onEntityRemoved(entityIn);
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

    @Inject(method = "getEntitiesWithinAABBExcludingEntity", at = @At("TAIL"), cancellable = true)
    public void getEntitiesWithinAABBExcludingEntity(@Nullable Entity entityIn, AxisAlignedBB bb,
            CallbackInfoReturnable<List<Entity>> info) {
        List<Entity> returnValue = info.getReturnValue();
        returnValue.removeIf(UltimateUtil::isUltimatePlayer);
        info.setReturnValue(returnValue);
    }

    @Inject(method = "getEntitiesInAABBexcluding", at = @At("TAIL"), cancellable = true)
    public void getEntitiesInAABBexcluding(@Nullable Entity entityIn, AxisAlignedBB boundingBox,
            @Nullable Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> info) {
        List<Entity> returnValue = info.getReturnValue();
        returnValue.removeIf(UltimateUtil::isUltimatePlayer);
        info.setReturnValue(returnValue);
    }

    @Inject(method = "getEntities", at = @At("TAIL"), cancellable = true)
    public <T extends Entity> void getEntities(Class<? extends T> entityType, Predicate<? super T> filter,
            CallbackInfoReturnable<List<T>> info) {
        List<T> returnValue = info.getReturnValue();
        returnValue.removeIf(UltimateUtil::isUltimatePlayer);
        info.setReturnValue(returnValue);
    }

    @Inject(method = "getPlayers", at = @At("TAIL"), cancellable = true)
    public <T extends Entity> void getPlayers(Class<? extends T> playerType, Predicate<? super T> filter,
            CallbackInfoReturnable<List<T>> info) {
        List<T> returnValue = info.getReturnValue();
        returnValue.removeIf(UltimateUtil::isUltimatePlayer);
        info.setReturnValue(returnValue);
    }

    @Inject(method = "getEntitiesWithinAABB", at = @At("TAIL"), cancellable = true)
    public <T extends Entity> void getEntitiesWithinAABB(Class<? extends T> classEntity, AxisAlignedBB bb,
            CallbackInfoReturnable<List<T>> info) {
        List<T> returnValue = info.getReturnValue();
        returnValue.removeIf(UltimateUtil::isUltimatePlayer);
        info.setReturnValue(returnValue);
    }

    @Inject(method = "getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;", at = @At("TAIL"), cancellable = true)
    public <T extends Entity> void getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB aabb,
            @Nullable Predicate<? super T> filter, CallbackInfoReturnable<List<T>> info) {
        List<T> returnValue = info.getReturnValue();
        returnValue.removeIf(UltimateUtil::isUltimatePlayer);
        info.setReturnValue(returnValue);
    }

    @Inject(method = "findNearestEntityWithinAABB", at = @At("TAIL"), cancellable = true)
    public <T extends Entity> void findNearestEntityWithinAABB(Class<? extends T> entityType, AxisAlignedBB aabb,
            T closestTo, CallbackInfoReturnable<T> info) {
        if (UltimateUtil.isUltimatePlayer(info.getReturnValue())) {
            info.setReturnValue(null);
        }
    }

    @Redirect(method = "updateEntityWithOptionalForce", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;canEntityUpdate(Lnet/minecraft/entity/Entity;)Z", remap = false))
    public boolean updateEntityWithOptionalForce(Entity entityIn) {
        if (UltimateUtil.isUltimatePlayer(entityIn)) {
            return true;
        }

        return ForgeEventFactory.canEntityUpdate(entityIn);
    }

    @Override
    public void fastRemove(Entity e) {
        e.isDead = true;
        int chunkCoordX = e.chunkCoordX;
        int chunkCoordZ = e.chunkCoordZ;

        if (e.addedToChunk && isChunkLoaded(chunkCoordX, chunkCoordZ, true)) {
            getChunkFromChunkCoords(chunkCoordX, chunkCoordZ).removeEntity(e);
        }

        onEntityRemoved(e);
    }

    @Override
    public void setTheWorld(boolean theWorld) {
        this.theWorld = theWorld;
        UltimateMod.getLogger().info("Set the world:{}", theWorld);
    }

    @Override
    public boolean isTheWorld() {
        return theWorld;
    }
}
