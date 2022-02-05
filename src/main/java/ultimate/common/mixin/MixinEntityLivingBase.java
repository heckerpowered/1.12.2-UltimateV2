package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ultimate.common.util.UltimateUtil;

@Mixin({ EntityLivingBase.class })
public class MixinEntityLivingBase {
    private EntityLivingBase entityLivingBase = (EntityLivingBase) (Object) this;

    @Shadow
    private ItemStack activeItemStack;

    @Inject(method = "getHealth", cancellable = true, at = @At("HEAD"))
    public void getHealth(CallbackInfoReturnable<Float> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(20.0F);
        } else if (UltimateUtil.isUltimateDead(entityLivingBase)) {
            info.setReturnValue(0.0F);
        }
    }

    @Inject(method = "setHealth", cancellable = true, at = @At("HEAD"))
    public void setHealth(float health, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }

    @Inject(method = "attackEntityFrom", cancellable = true, at = @At("HEAD"))
    public void attackEntityFrom(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "addPotionEffect", cancellable = true, at = @At("HEAD"))
    public void addPotionEffect(PotionEffect potioneffectIn, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase) && potioneffectIn.getPotion().isBadEffect()) {
            info.cancel();
        }
    }

    @Inject(method = "knockBack", cancellable = true, at = @At("HEAD"))
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }

    @Inject(method = "isEntityAlive", cancellable = true, at = @At("HEAD"))
    public void isEntityAlive(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "damageEntity", cancellable = true, at = @At("HEAD"))
    public void damageEntity(DamageSource damageSrc, float damageAmount, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }

    @Inject(method = "getMaxHealth", cancellable = true, at = @At("HEAD"))
    public void getMaxHealth(CallbackInfoReturnable<Float> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(20.0F);
            return;
        }

        if (UltimateUtil.isUltimateDead(entityLivingBase)) {
            info.setReturnValue(0.0F);
        }
    }

    @Inject(method = "getWaterSlowDown", cancellable = true, at = @At("HEAD"))
    public void getWaterSlowDown(CallbackInfoReturnable<Float> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(0F);
        }
    }

    @Inject(method = "getAIMoveSpeed", cancellable = true, at = @At("HEAD"))
    public void getAIMoveSpeed(CallbackInfoReturnable<Float> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(((EntityPlayer) entityLivingBase).isSprinting() ? 0.13000001F : 0.1F);
        }
    }

    @Inject(method = "setAIMoveSpeed", cancellable = true, at = @At("HEAD"))
    public void setAIMoveSpeed(float speedIn, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }

    @Inject(method = "onDeath", cancellable = true, at = @At("HEAD"))
    public void onDeath(DamageSource cause, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }

    @Inject(method = "onDeathUpdate", cancellable = true, at = @At("HEAD"))
    public void onDeathUpdate(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }

    @Inject(method = "getHurtSound", cancellable = true, at = @At("HEAD"))
    public void getHurtSound(DamageSource damageSourceIn, CallbackInfoReturnable<SoundEvent> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(null);
        }
    }

    @Inject(method = "playHurtSound", cancellable = true, at = @At("HEAD"))
    protected void playHurtSound(DamageSource source, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }

    @Inject(method = "performHurtAnimation", cancellable = true, at = @At("HEAD"))
    @SideOnly(Side.CLIENT)
    public void performHurtAnimation(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }

    @Inject(method = "attackable", cancellable = true, at = @At("HEAD"))
    public void attackable(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "canBeHitWithPotion", cancellable = true, at = @At("HEAD"))
    public void canBeHitWithPotion(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "markVelocityChanged", cancellable = true, at = @At("HEAD"))
    public void markVelocityChanged(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }

    @Inject(method = "canBeCollidedWith", cancellable = true, at = @At("HEAD"))
    public void canBeCollidedWith(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "canBePushed", cancellable = true, at = @At("HEAD"))
    public void canBePushed(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "canBlockDamageSource", cancellable = true, at = @At("HEAD"))
    public void canBlockDamageSource(DamageSource damageSourceIn, CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "isMovementBlocked", cancellable = true, at = @At("HEAD"))
    public void isMovementBlocked(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.setReturnValue(false);
        }
    }

    @Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeHooks;onLivingUpdate(Lnet/minecraft/entity/EntityLivingBase;)Z", remap = false))
    public boolean redirect$onUpdate(EntityLivingBase entityLivingBase) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            return false;
        }

        return ForgeHooks.onLivingUpdate(entityLivingBase);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo info) {
        if (UltimateUtil.isUltimateDead(entityLivingBase)) {
        }
    }

    @Inject(method = "stopActiveHand", at = @At("HEAD"), cancellable = true)
    public void stopActiveHand(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            if (!this.activeItemStack.isEmpty()) {
                this.activeItemStack.onPlayerStoppedUsing(
                        entityLivingBase.world, (EntityLivingBase) (Object) this, 0);
            }

            entityLivingBase.resetActiveHand();
            info.cancel();
        }
    }

    @Inject(method = "updateActiveHand", at = @At("HEAD"), cancellable = true)
    public void updateActiveHand(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            entityLivingBase.getHeldItemMainhand().onPlayerStoppedUsing(
                    entityLivingBase.world, (EntityLivingBase) (Object) this, 0);
            entityLivingBase.getHeldItemOffhand().onPlayerStoppedUsing(
                    entityLivingBase.world, (EntityLivingBase) (Object) this, 0);
            info.cancel();
        }
    }

    @Inject(method = "fall", cancellable = true, at = @At("HEAD"))
    public void fall(float distance, float damageMultiplier, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }

    @Inject(method = "outOfWorld", cancellable = true, at = @At("HEAD"))
    public void outOfWorld(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLivingBase)) {
            info.cancel();
        }
    }
}
