package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import ultimate.common.util.UltimateUtil;

@Mixin({ EntityPlayerMP.class })
public class MixinEntityPlayerMP {

    private EntityPlayerMP player = (EntityPlayerMP) (Object) this;

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onUpdate(CallbackInfo info) {
        if (UltimateUtil.isUltimateDead(player)) {
            player.setSpawnPoint(player.getPosition(), true);
            player.isDead = false;
            player.deathTime = 0;
            player.maxHurtTime = 0;
            player.world.setEntityState(player, (byte) 0);
        }
    }

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

    @Inject(method = "onDeath", cancellable = true, at = @At("HEAD"))
    public void onDeath(DamageSource cause, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.cancel();
        }
    }
}
