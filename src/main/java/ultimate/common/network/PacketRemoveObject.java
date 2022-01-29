package ultimate.common.network;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
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
            minecraft.currentScreen = guiScreen;

            minecraft.setIngameNotInFocus();
            KeyBinding.unPressAllKeys();

            while (Mouse.next()) {
                ;
            }

            while (Keyboard.next()) {
                ;
            }

            ScaledResolution scaledresolution = new ScaledResolution(minecraft);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiScreen.setWorldAndResolution(minecraft, i, j);
            minecraft.skipRenderWorld = false;
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
