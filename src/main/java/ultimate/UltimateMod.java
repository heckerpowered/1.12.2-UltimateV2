package ultimate;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
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
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "ultimate";

    @SidedProxy(serverSide = "ultimate.common.CommonProxy", clientSide = "ultimate.client.ClientProxy")
    public static CommonProxy proxy;

    public UltimateMod() {

    }

    @EventHandler
    @SuppressWarnings("unchecked")
    public void onPreInitialize(FMLPreInitializationEvent event) {
        proxy.onPreInitialize(event);
        Loader instance = Loader.instance();
        try {
            Field field = Loader.class.getDeclaredField("mods");
            field.setAccessible(true);
            List<ModContainer> list = ((List<ModContainer>) field.get(instance));
            List<ModContainer> mods = Lists.newArrayList();

            int size = list.size();
            for (int i = 0; i < size; i++) {
                mods.add(null);
            }
            Collections.copy(mods, list);

            mods.removeIf(m -> {
                System.out.print(m.getName());
                return m.getName().equals(MODID);
            });
            field.set(instance, ImmutableList.copyOf(mods));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
