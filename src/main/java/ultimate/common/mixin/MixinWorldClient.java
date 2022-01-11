package ultimate.common.mixin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import ultimate.UltimateMod;
import ultimate.common.util.UltimateEntitySet;
import ultimate.common.util.UltimateUtil;

@Mixin({ WorldClient.class })
public abstract class MixinWorldClient extends World {
    private static final Field ENTITY_LIST = ObfuscationReflectionHelper.findField(WorldClient.class,
            "field_73032_d");
    static {
        ENTITY_LIST.setAccessible(true);
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(ENTITY_LIST, ENTITY_LIST.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            UltimateMod.getLogger().error("Error initializing WorldClientMixin.", e);
        }
    }

    private WorldClient world = (WorldClient) (Object) this;

    @Shadow
    @Final
    private Set<Entity> entityList;

    protected MixinWorldClient(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn,
            Profiler profilerIn, boolean client) {
        super(saveHandlerIn, info, providerIn, profilerIn, client);
    }

    @Inject(method = "removeEntity", at = @At("HEAD"), cancellable = true)
    public void removeEntity(Entity entityIn, CallbackInfo info) {
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

    @Inject(method = "removeEntityFromWorld", at = @At("HEAD"), cancellable = true)
    public void removeEntityFromWorld(int entityID, CallbackInfoReturnable<Entity> info) {
        if (UltimateUtil.isUltimatePlayer(entitiesById.lookup(entityID))) {
            info.setReturnValue(null);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo info) {
        if (!(entityList instanceof UltimateEntitySet)) {
            try {
                ENTITY_LIST.set(world, new UltimateEntitySet(loadedEntityList));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                UltimateMod.getLogger().error("Error initializing WorldClientMixin.", e);
                info.cancel();
            }
        }
    }
}
