package ultimate.common.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ultimate.common.util.UltimateUtil;

@SideOnly(Side.CLIENT)
@Mixin({ EntityRenderer.class })
public class MixinEntityRenderer {
    @Redirect(method = "getFOVModifier", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;deathTime:I", opcode = Opcodes.GETFIELD))
    private int deathTime$getFOVModifier(EntityLivingBase entity) {
        Minecraft minecraft = Minecraft.getMinecraft();

        if (UltimateUtil.isUltimatePlayer(minecraft.player)) {
            return 0;
        } else if (UltimateUtil.isUltimateDead(minecraft.player)) {
            return UltimateUtil.getUltimateDeathTime(minecraft.player);
        }

        return entity.deathTime;
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(float partialTicks, CallbackInfo info) {
        Minecraft minecraft = Minecraft.getMinecraft();

        if (UltimateUtil.isUltimatePlayer(minecraft.player)) {
            info.cancel();
        }
    }

    @Redirect(method = "hurtCameraEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getHealth()F"))
    private float redirect_getHealth_hurtCameraEffect(EntityLivingBase e) {
        if (UltimateUtil.isUltimatePlayer(e)) {
            return 20.0F;
        } else if (UltimateUtil.isUltimateDead(e)) {
            return 0.0F;
        }

        return e.getHealth();
    }

    @Redirect(method = "hurtCameraEffect", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;deathTime:I"))
    private int redirect_deathTime_hurtCameraEffect(EntityLivingBase e) {
        if (UltimateUtil.isUltimatePlayer(e)) {
            return 0;
        } else if (UltimateUtil.isUltimateDead(e)) {
            return UltimateUtil.getUltimateDeathTime(e);
        }

        return e.deathTime;
    }

    @Redirect(method = "hurtCameraEffect", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;hurtTime:I"))
    private int redirect_hurtTime_hurtCameraEffect(EntityLivingBase e) {
        if (UltimateUtil.isUltimatePlayer(e)) {
            return 0;
        } else if (UltimateUtil.isUltimateDead(e)) {
            int hurtTime = 10 - UltimateUtil.getUltimateDeathTime(e);
            if (hurtTime < 0) {
                hurtTime = 0;
            }

            return hurtTime;
        }

        return e.hurtTime;
    }

    @Redirect(method = "hurtCameraEffect", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;maxHurtTime:I"))
    private int redirect_maxHurtTime_hurtCameraEffect(EntityLivingBase e) {
        if (UltimateUtil.isUltimatePlayer(e)) {
            return 0;
        } else if (UltimateUtil.isUltimateDead(e)) {
            return 10;
        }

        return e.maxHurtTime;
    }
}
