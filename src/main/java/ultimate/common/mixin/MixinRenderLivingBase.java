package ultimate.common.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
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
            GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
            float f = ((float) UltimateUtil.getUltimateDeathTime(entityLiving) + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);

            if (f > 1.0F) {
                f = 1.0F;
            }

            GlStateManager.rotate(f * 90.0F, 0.0F, 0.0F, 1.0F);
            info.cancel();
        }
    }

    @Redirect(method = "setBrightness", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;deathTime:I", opcode = Opcodes.GETFIELD))
    protected int deathTime$setBrightness(T entitylivingbaseIn) {
        if (UltimateUtil.isUltimatePlayer(entitylivingbaseIn)) {
            return 0;
        } else if (UltimateUtil.isUltimateDead(entitylivingbaseIn)) {
            return UltimateUtil.getUltimateDeathTime(entitylivingbaseIn);
        }

        return entitylivingbaseIn.deathTime;
    }

    @Redirect(method = "setBrightness", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;hurtTime:I", opcode = Opcodes.GETFIELD))
    protected int hurtTime$setBrightness(T entitylivingbaseIn) {
        if (UltimateUtil.isUltimatePlayer(entitylivingbaseIn)) {
            return 0;
        }

        return entitylivingbaseIn.hurtTime;
    }
}
