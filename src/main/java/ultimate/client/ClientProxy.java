package ultimate.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
}
