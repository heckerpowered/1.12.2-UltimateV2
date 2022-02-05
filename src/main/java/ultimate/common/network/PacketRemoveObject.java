package ultimate.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketRemoveObject implements IMessageHandler<PacketRemoveObject.MessageRemoveObject, IMessage> {

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(PacketRemoveObject.MessageRemoveObject message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            Minecraft minecraft = Minecraft.getMinecraft();
            GuiScreen guiScreen = new GuiGameOver(null);
            minecraft.displayGuiScreen(guiScreen);
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
