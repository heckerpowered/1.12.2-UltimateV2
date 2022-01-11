package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import ultimate.common.util.UltimateUtil;

@Mixin({ RenderLivingBase.class })
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> {
    @Inject(method = "applyRotations", cancellable = true, at = @At("HEAD"))
    protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks,
            CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(entityLiving)) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            player.swingProgress += 100;
            GlStateManager.rotate(
                    (((float) entityLiving.ticksExisted + partialTicks - 1.0F) / 20.0F * 1.6F) * 90F * 10,
                    0.0F,
                    10.0F, 0.0F);
            info.cancel();
        } else if (UltimateUtil.isUltimateDead(entityLiving)) {
            entityLiving.deathTime = UltimateUtil.getUltimateDeathTime(entityLiving);
        }
    }

    @Inject(method = "setBrightness", at = @At("HEAD"))
    protected void setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures,
            CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entitylivingbaseIn)) {
            entitylivingbaseIn.deathTime = 0;
            entitylivingbaseIn.hurtTime = 0;
        } else if (UltimateUtil.isUltimateDead(entitylivingbaseIn)) {
            entitylivingbaseIn.deathTime = UltimateUtil.getUltimateDeathTime(entitylivingbaseIn);
        }
    }
}
