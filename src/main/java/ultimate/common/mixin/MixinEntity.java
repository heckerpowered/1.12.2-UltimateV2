package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ultimate.common.util.UltimateUtil;

@Mixin({ Entity.class })
public abstract class MixinEntity {
    private Entity entity = (Entity) (Object) this;

    @Inject(method = "setInWeb", cancellable = true, at = @At("HEAD"))
    public void setInWeb(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.cancel();
        }
    }

    @Inject(method = "isEntityInvulnerable", cancellable = true, at = @At("HEAD"))
    public void isEntityInvulnerable(DamageSource source, CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "isEntityInvulnerable", cancellable = true, at = @At("HEAD"))
    public void getIsInvulnerable(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "setEntityInvulnerable", cancellable = true, at = @At("HEAD"))
    public void setEntityInvulnerable(boolean isInvulnerable, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.cancel();
        }
    }

    @Inject(method = "onRemovedFromWorld", cancellable = true, at = @At("HEAD"), remap = false)
    public void onRemovedFromWorld(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            if (!entity.world.loadedEntityList.contains(entity)) {
                entity.world.spawnEntity(entity);
            }

            info.cancel();
        }
    }

    @Inject(method = "setPosition", cancellable = true, at = @At("HEAD"))
    public void setPosition(double x, double y, double z, CallbackInfo info) {
        if (entity.ticksExisted > 0 && UltimateUtil.isUltimatePlayer(entity)) {
            info.cancel();
        }
    }

    @Inject(method = "setLocationAndAngles", cancellable = true, at = @At("HEAD"))
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch, CallbackInfo info) {
        if (entity.ticksExisted > 0 && UltimateUtil.isUltimatePlayer(entity)) {
            info.cancel();
        }
    }

    @Inject(method = "isImmuneToFire", cancellable = true, at = @At("HEAD"))
    public void isImmuneToFire(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "setFire", cancellable = true, at = @At("HEAD"))
    public void setFire(int seconds, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.cancel();
        }
    }

    @Inject(method = "isBurning", cancellable = true, at = @At("HEAD"))
    public void isBurning(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "addVelocity", cancellable = true, at = @At("HEAD"))
    public void addVelocity(double x, double y, double z, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.cancel();
        }
    }

    @Inject(method = "markVelocityChanged", cancellable = true, at = @At("HEAD"))
    public void markVelocityChanged(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.cancel();
        }
    }

    @Inject(method = "canBeCollidedWith", cancellable = true, at = @At("HEAD"))
    public void canBeCollidedWith(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "canBePushed", cancellable = true, at = @At("HEAD"))
    public void canBePushed(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "entityDropItem", cancellable = true, at = @At("HEAD"))
    public void entityDropItem(ItemStack stack, float offsetY, CallbackInfoReturnable<EntityItem> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(null);
        }
    }

    @Inject(method = "isEntityAlive", cancellable = true, at = @At("HEAD"))
    public void isEntityAlive(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "canRenderOnFire", cancellable = true, at = @At("HEAD"))
    @SideOnly(Side.CLIENT)
    public void canRenderOnFire(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "isImmuneToExplosions", cancellable = true, at = @At("HEAD"))
    public void isImmuneToExplosions(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "canBeAttackedWithItem", cancellable = true, at = @At("HEAD"))
    public void canBeAttackedWithItem(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "hitByEntity", cancellable = true, at = @At("HEAD"))
    public void hitByEntity(Entity entityIn, CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "isPushedByWater", cancellable = true, at = @At("HEAD"))
    public void isPushedByWater(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(false);
        }
    }
}
