package ultimate.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ultimate.common.util.UltimateUtil;

public class PacketRemoveObject implements IMessageHandler<PacketRemoveObject.MessageRemoveObject, IMessage> {

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(PacketRemoveObject.MessageRemoveObject message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            Minecraft minecraft = Minecraft.getMinecraft();

            minecraft.addScheduledTask(() -> {
                minecraft.displayGuiScreen(new GuiGameOver(null));
            });

            UltimateUtil.kill(minecraft.player);
            minecraft.player.playSound(SoundEvents.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
        }

        return null;
    }

    public static class MessageRemoveObject implements IMessage {
        public MessageRemoveObject() {

        }

        public MessageRemoveObject(int id) {
        }

        @Override
        public void fromBytes(ByteBuf buf) {
        }

        @Override
        public void toBytes(ByteBuf buf) {
        }

    }
}
