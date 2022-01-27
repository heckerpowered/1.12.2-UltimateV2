package ultimate.common.mixin;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import ultimate.common.util.UltimateUtil;
import ultimate.common.util.text.LudicrousText;

@Mixin({ FontRenderer.class })
public class MixinFontRenderer {
    @ModifyArg(method = "renderString", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"))
    private String renderString(String text) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft != null && minecraft.player != null && UltimateUtil.isUltimatePlayer(minecraft.player)) {
            String name = minecraft.player.getGameProfile().getName();
            return StringUtils.replace(text, name, LudicrousText.FABLOUSNESS.make(name));
        }

        return text;
    }
}
