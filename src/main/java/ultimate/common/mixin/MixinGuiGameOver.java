package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import ultimate.common.util.UltimateUtil;

@Mixin({ GuiGameOver.class })
public class MixinGuiGameOver {
    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (UltimateUtil.isUltimatePlayer(minecraft.player)) {
            info.cancel();
        }
    }
}
