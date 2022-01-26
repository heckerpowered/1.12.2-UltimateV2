package ultimate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import ultimate.common.CommonProxy;
import ultimate.common.command.CommandRemake;
import ultimate.common.network.PacketHandler;

@Mod(modid = UltimateMod.MODID)
public final class UltimateMod {
    public static final String MODID = "ultimate";
    private static final Logger LOGGER = LogManager.getLogger();

    @SidedProxy(serverSide = "ultimate.common.CommonProxy", clientSide = "ultimate.client.ClientProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void onPreInitialize(FMLPreInitializationEvent event) {
        proxy.onPreInitialize(event);
    }

    @EventHandler
    public void onInitialize(FMLInitializationEvent event) {
        proxy.onInitialize(event);
        PacketHandler.initialize();
    }

    @EventHandler
    public void onPostInitialize(FMLPostInitializationEvent event) {
        proxy.onPostInitialize(event);
    }

    @EventHandler
    public void onServerStarting(final FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandRemake());
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
