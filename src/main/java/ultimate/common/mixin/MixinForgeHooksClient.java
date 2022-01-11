package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ForgeHooksClient;
import ultimate.client.util.ClientUtil;
import ultimate.common.util.UltimateUtil;

@Mixin({ ForgeHooksClient.class })
public class MixinForgeHooksClient {
    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true, remap = false)
    private static void drawScreen(GuiScreen screen, int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.player != null) {
            if (UltimateUtil.isUltimatePlayer(minecraft.player)
                    && ClientUtil.isGameOverGuiScreen(screen)) {
                info.cancel();
            }
        }
    }
}
