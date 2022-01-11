package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.text.ITextComponent;
import ultimate.common.util.UltimateUtil;

@Mixin({ NetHandlerPlayServer.class })
public class MixinNetHandlerPlayServer {
    @Shadow
    private EntityPlayerMP player;

    public void disconnect(final ITextComponent textComponent, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.cancel();
        }
    }
}
