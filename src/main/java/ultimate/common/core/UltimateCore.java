package ultimate.common.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;

@SortingIndex(value = Integer.MIN_VALUE)
public class UltimateCore implements IFMLLoadingPlugin {
    public static void initMixin() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.ultimate.json");
    }

    public void init() {
        try {
            ClassLoader appClassLoader = Launch.class.getClassLoader();
            MethodUtils.invokeMethod(appClassLoader, true, "addURL",
                    this.getClass().getProtectionDomain().getCodeSource().getLocation());
            MethodUtils.invokeStaticMethod(appClassLoader.loadClass(this.getClass().getName()), "initMixin");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UltimateCore()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        init();
        Field field = LaunchClassLoader.class.getDeclaredField("transformers");
        field.setAccessible(true);
        field.set(Launch.classLoader,
                new UltimateTransformersList(Launch.classLoader.getTransformers()));
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.set(field, field.getModifiers() | Modifier.FINAL);
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
