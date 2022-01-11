package ultimate.common.registry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import ultimate.common.item.ItemUltimateSword;

@EventBusSubscriber
public final class UltimateItemLoader {
    public static final ItemUltimateSword ITEM_ULTIMATE_SWORD = new ItemUltimateSword();

    @SubscribeEvent
    public static void onItemRegistry(Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(ITEM_ULTIMATE_SWORD);
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent modelRegistryEvent) {
        registerItemRenderer(ITEM_ULTIMATE_SWORD, 0, "inventory");
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}
