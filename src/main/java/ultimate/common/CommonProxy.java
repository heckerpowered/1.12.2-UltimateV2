package ultimate.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ultimate.common.event.CommonPlayerEvent;

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
}
