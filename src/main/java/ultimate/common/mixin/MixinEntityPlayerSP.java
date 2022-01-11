package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import ultimate.common.util.UltimateUtil;

@Mixin({ EntityPlayerSP.class })
public class MixinEntityPlayerSP {
    // private EntityPlayerSP player = (EntityPlayerSP) (Object) this;

    @Inject(method = "onCriticalHit", at = @At("HEAD"), cancellable = true)
    public void onCriticalHit(Entity entityHit, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityHit)) {
            info.cancel();
        }
    }

    @Inject(method = "onEnchantmentCritical", at = @At("HEAD"), cancellable = true)
    public void onEnchantmentCritical(Entity entityHit, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityHit)) {
            info.cancel();
        }
    }
}
