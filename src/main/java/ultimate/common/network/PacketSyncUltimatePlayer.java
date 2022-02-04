package ultimate.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ultimate.common.util.UltimateUtil;

public class PacketSyncUltimatePlayer
        implements IMessageHandler<PacketSyncUltimatePlayer.MessageSyncUltimatePlayer, IMessage> {
    public static class MessageSyncUltimatePlayer implements IMessage {
        private String name;

        public MessageSyncUltimatePlayer() {
        }

        public MessageSyncUltimatePlayer(String name) {
            this.name = name;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            byte[] buffer = new byte[buf.readableBytes()];
            buf.readBytes(buffer);
            name = new String(buffer);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBytes(name.getBytes());
        }

        public String getName() {
            return name;
        }

    }

    @Override
    public IMessage onMessage(MessageSyncUltimatePlayer message, MessageContext ctx) {
        UltimateUtil.addUltimatePlayer(message.getName());
        return null;
    }
}
