package ultimate.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ultimate.common.event.CommonPlayerEvent;
import net.minecraft.world.WorldServer;

public class CommonProxy {
    protected boolean clientLoaded;

    public boolean isServer() {
        return !clientLoaded;
    }

    public boolean isClient() {
        return clientLoaded;
    }

    public void onPreInitialize(FMLPreInitializationEvent event) {
    }

    public void onInitialize(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(CommonPlayerEvent.class);
    }

    public void onPostInitialize(FMLPostInitializationEvent event) {

    }

    public EntityPlayer getPlayer(MessageContext context) {
        return context.getServerHandler().player;
    }

    public void handlePacket(Runnable runnable, EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            ((WorldServer) player.world).addScheduledTask(runnable);
        }
    }

    public boolean isUltimatePlayer(String string) {
        return false;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
