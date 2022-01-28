package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import ultimate.common.util.UltimateUtil;

@Mixin({ ForgeHooks.class })
public class MixinForgeHooks {
    @Inject(method = "onLivingUpdate", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onLivingUpdate(EntityLivingBase entity, CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "onLivingAttack", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onLivingAttack(EntityLivingBase entity, DamageSource src, float amount,
            CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(false);
        }
        if (src.getTrueSource() != null && UltimateUtil.isUltimatePlayer(src.getTrueSource())) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "onLivingKnockBack", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onLivingKnockBack(EntityLivingBase target, Entity attacker, float strength,
            double ratioX, double ratioZ, CallbackInfoReturnable<LivingKnockBackEvent> info) {
        LivingKnockBackEvent event = new LivingKnockBackEvent(target, attacker, strength, ratioX, ratioZ);
        if (UltimateUtil.isUltimatePlayer(target)) {
            event.setCanceled(true);
            info.setReturnValue(event);
        }
    }

    @Inject(method = "onLivingHurt", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onLivingHurt(EntityLivingBase entity, DamageSource src, float amount,
            CallbackInfoReturnable<Float> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(0F);
        }
        if (src.getTrueSource() != null && UltimateUtil.isUltimatePlayer(src.getTrueSource())) {
            info.setReturnValue(amount);
        }
    }

    @Inject(method = "onLivingDamage", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onLivingDamage(EntityLivingBase entity, DamageSource src, float amount,
            CallbackInfoReturnable<Float> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(0F);
        }
        if (src.getTrueSource() != null && UltimateUtil.isUltimatePlayer(src.getTrueSource())) {
            info.setReturnValue(amount);
        }
    }

    @Inject(method = "onLivingDeath", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onLivingDeath(EntityLivingBase entity, DamageSource src, CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(true);
        }
        if (src.getTrueSource() != null && UltimateUtil.isUltimatePlayer(src.getTrueSource())) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "onPlayerAttackTarget", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onPlayerAttackTarget(EntityPlayer player, Entity target, CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(target)) {
            info.setReturnValue(true);
        }
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "onFarmlandTrample", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onFarmlandTrample(World world, BlockPos pos, IBlockState state, float fallDistance,
            Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            info.setReturnValue(true);
        }
    }

}
