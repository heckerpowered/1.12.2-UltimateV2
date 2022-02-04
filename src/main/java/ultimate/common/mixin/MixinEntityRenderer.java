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
            return UltimateUtil.increaseUltimateDeathTime(minecraft.player);
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
}
