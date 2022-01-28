package ultimate.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import ultimate.UltimateMod;
import ultimate.common.network.PacketRemoveObject.MessageRemoveObject;

public class PacketHandler {
    private static boolean initialized;
    public static final SimpleNetworkWrapper NETWOR_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("ULTIMATE");

    public static void initialize() {
        if (initialized) {
            throw new UnsupportedOperationException();
        }

        NETWOR_WRAPPER.registerMessage(PacketRemoveObject.class, MessageRemoveObject.class, 0, Side.CLIENT);
    }

    /**
     * Encodes an Object[] of data into a DataOutputStream.
     *
     * @param dataValues - an Object[] of data to encode
     * @param out        - the output stream to write to
     */
    public static void encode(Object[] dataValues, ByteBuf out) {
        for (Object data : dataValues) {
            encode(out, data, data.getClass());
        }
    }

    /**
     * Encode an Object of data into a DataOutputStream
     *
     * @param data an Object of data to encode
     * @param out  the output stream to write to
     */
    public static void encode(Object data, ByteBuf out) {
        encode(out, data, data.getClass());
    }

    private static final void encode(ByteBuf out, Object data, Class<?> type) {
        if (type.isPrimitive() /* Moderately improve performance (?) */ ) {
            encodePrimitive(out, data, type);
        } else {
            encodeReference(out, data, type);
        }
    }

    private static void encodeReference(ByteBuf out, Object data, Class<?> type) {
        if (data instanceof String) {
            ByteBufUtils.writeUTF8String(out, (String) data);
        } else if (data instanceof ItemStack) {
            ByteBufUtils.writeItemStack(out, (ItemStack) data);
        } else {
            UltimateMod.getLogger().error("Un-encodable data passed to encode():{}", data);
        }
    }

    private static void encodePrimitive(ByteBuf out, Object data, Class<?> type) {
        // int.class equals Integer.class (?)
        if (Byte.TYPE.isAssignableFrom(type)) {
            out.writeByte((byte) data);
        } else if (Integer.TYPE.isAssignableFrom(type)) {
            out.writeInt((int) data);
        } else if (Short.TYPE.isAssignableFrom(type)) {
            out.writeShort((short) data);
        } else if (Long.TYPE.isAssignableFrom(type)) {
            out.writeLong((long) data);
        } else if (Boolean.TYPE.isAssignableFrom(type)) {
            out.writeBoolean((boolean) data);
        } else if (Double.TYPE.isAssignableFrom(type)) {
            out.writeDouble((double) data);
        } else if (Float.TYPE.isAssignableFrom(type)) {
            out.writeFloat((float) data);
        } else {
            UltimateMod.getLogger().error("Unknow primitive class type:{},data:{}", type, data);
        }
    }

    public static void handlePacket(Runnable runnable, EntityPlayer player) {
        UltimateMod.proxy.handlePacket(runnable, player);
    }

    public static EntityPlayer getPlayer(MessageContext context) {
        return UltimateMod.proxy.getPlayer(context);
    }

    /**
     * Send this message to the specified player.
     * The {@link IMessageHandler} for this message type should be on the CLIENT
     * side.
     *
     * @param message The message to send
     * @param player  The player to send it to
     */
    public static void sendTo(IMessage message, EntityPlayerMP player) {
        NETWOR_WRAPPER.sendTo(message, player);
    }

    /**
     * Send this message to everyone.
     * The {@link IMessageHandler} for this message type should be on the CLIENT
     * side.
     *
     * @param message The message to send
     */
    public static void sendToAll(IMessage message) {
        NETWOR_WRAPPER.sendToAll(message);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * The {@link IMessageHandler} for this message type should be on the CLIENT
     * side.
     *
     * @param message The message to send
     * @param point   The {@link TargetPoint} around which to send
     */
    public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        NETWOR_WRAPPER.sendToAllAround(message, point);
    }

    /**
     * Sends this message to everyone tracking a point.
     * The {@link IMessageHandler} for this message type should be on the CLIENT
     * side.
     * The {@code range} field of the {@link TargetPoint} is ignored.
     *
     * @param message The message to send
     * @param point   The tracked {@link TargetPoint} around which to send
     */
    public static void sendToAllTracking(IMessage message, NetworkRegistry.TargetPoint point) {
        NETWOR_WRAPPER.sendToAllTracking(message, point);
    }

    /**
     * Sends this message to everyone tracking an entity.
     * The {@link IMessageHandler} for this message type should be on the CLIENT
     * side.
     * This is not equivalent to {@link #sendToAllTracking(IMessage, TargetPoint)}
     * because entities have different tracking distances based on their type.
     *
     * @param message The message to send
     * @param entity  The tracked entity around which to send
     */
    public static void sendToAllTracking(IMessage message, Entity entity) {
        NETWOR_WRAPPER.sendToAllTracking(message, entity);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * The {@link IMessageHandler} for this message type should be on the CLIENT
     * side.
     *
     * @param message     The message to send
     * @param dimensionId The dimension id to target
     */
    public static void sendToDimension(IMessage message, int dimensionId) {
        NETWOR_WRAPPER.sendToDimension(message, dimensionId);
    }

    /**
     * Send this message to the server.
     * The {@link IMessageHandler} for this message type should be on the SERVER
     * side.
     *
     * @param message The message to send
     */
    public static void sendToServer(IMessage message) {
        NETWOR_WRAPPER.sendToServer(message);
    }
}
