package ultimate.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRemoveObject implements IMessageHandler<PacketRemoveObject.MessageRemoveObject, IMessage> {

    @Override
    public IMessage onMessage(PacketRemoveObject.MessageRemoveObject message, MessageContext ctx) {
        return null;
    }

    public static class MessageRemoveObject implements IMessage {
        private int entityId;

        public MessageRemoveObject() {

        }

        public MessageRemoveObject(int id) {
            entityId = id;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            entityId = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            PacketHandler.encode(entityId, buf);
        }

    }
}
