package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ultimate.common.util.UltimateUtil;

@Mixin({ FMLCommonHandler.class })
public class MixinFMLCommonHandler {
    @Inject(method = "onRenderTickStart", at = @At("HEAD"), cancellable = true, remap = false)
    public void onRenderTickStart(float timer, CallbackInfo info) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;
        if (player != null && UltimateUtil.isUltimatePlayer(player)) {
            info.cancel();
        }
    }

    @Inject(method = "onRenderTickEnd", at = @At("HEAD"), cancellable = true, remap = false)
    public void onRenderTickEnd(float timer, CallbackInfo info) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;
        if (player != null && UltimateUtil.isUltimatePlayer(player)) {
            info.cancel();
        }
    }
}
