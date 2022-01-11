package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import ultimate.common.util.UltimateUtil;

@Mixin({ CombatTracker.class })
public class MixinCombatTracker {
    @Shadow
    @Final
    private EntityLivingBase fighter;

    @Inject(method = "trackDamage", at = @At("HEAD"), cancellable = true)
    public void trackDamage(DamageSource damageSrc, float healthIn, float damageAmount, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(fighter)) {
            info.cancel();
        }
    }

    @Inject(method = "getDeathMessage", at = @At("HEAD"), cancellable = true)
    public void getDeathMessage(CallbackInfoReturnable<ITextComponent> info) {
        if (UltimateUtil.isUltimatePlayer(fighter)) {
            info.setReturnValue(null);
        }
    }
}
