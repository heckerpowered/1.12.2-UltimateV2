package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.FakePlayerFactory;
import ultimate.common.util.UltimateUtil;

@Mixin({ NetHandlerPlayServer.class })
public class MixinNetHandlerPlayServer {
    @Shadow
    private EntityPlayerMP player;

    @Inject(method = "disconnect", at = @At("HEAD"), cancellable = true)
    public void disconnect(final ITextComponent textComponent, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.cancel();
        }
    }

    @ModifyArg(method = "handleSlashCommand(Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/ICommandManager;executeCommand(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)I"))
    private ICommandSender adjustPlayer(ICommandSender sender) {
        if (UltimateUtil.isUltimatePlayer(sender)) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            return FakePlayerFactory.get(player.getServerWorld(), player.getGameProfile());
        }

        return sender;
    }
}
