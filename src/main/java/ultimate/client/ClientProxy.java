package ultimate.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ultimate.UltimateMod;
import ultimate.client.event.ClientEventHandler;
import ultimate.common.CommonProxy;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        clientLoaded = true;
    }

    @Override
    public void onPreInitialize(FMLPreInitializationEvent event) {
        super.onPreInitialize(event);
    }

    @Override
    public void onInitialize(FMLInitializationEvent event) {
        super.onInitialize(event);
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
    }

    @Override
    public void onPostInitialize(FMLPostInitializationEvent event) {
        super.onPostInitialize(event);
    }

    @Override
    public EntityPlayer getPlayer(MessageContext context) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            return context.getServerHandler().player;
        }

        return Minecraft.getMinecraft().player;
    }

    @Override
    public final void handlePacket(Runnable runnable, EntityPlayer player) {
        if (player == null || player.world.isRemote) {
            Minecraft.getMinecraft().addScheduledTask(runnable);
        } else {
            // Single player
            if (player.world instanceof WorldServer) {
                ((WorldServer) player.world).addScheduledTask(runnable);
            } else {
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                if (server != null) {
                    server.addScheduledTask(runnable);
                } else {
                    UltimateMod.getLogger().error(
                            "Packet handler wanted to set a scheduled task, but we couldn't find a way to set one.");
                    UltimateMod.getLogger().error("Player = {}, World = {}", player, player.world);
                }
            }
        }
    }

    @Override
    public boolean isUltimatePlayer(String string) {
        return false;
    }

    @Override
    public long getCurrentTime() {
        return Minecraft.getSystemTime();
    }
}
