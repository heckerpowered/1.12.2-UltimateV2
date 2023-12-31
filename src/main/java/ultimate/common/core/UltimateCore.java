package ultimate.common.core;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.Map;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;

@SortingIndex(value = Integer.MIN_VALUE)
public class UltimateCore implements IFMLLoadingPlugin {

    public void init() {
        try {
            ClassLoader appClassLoader = Launch.class.getClassLoader();
            //fix by mx_wj
            sun.misc.URLClassPath urlClassPath = sun.misc.SharedSecrets.getJavaNetAccess().getURLClassPath((URLClassLoader)appClassLoader);
            urlClassPath.addURL(UltimateCore.getJarURL(UltimateCore.class));
            sun.UltimateMixinLoader.initMixin();
            //fix end

            //old code
            /*MethodUtils.invokeMethod(appClassLoader, true, "addURL",
                    this.getClass().getProtectionDomain().getCodeSource().getLocation());
            MethodUtils.invokeStaticMethod(appClassLoader.loadClass(this.getClass().getName()), "initMixin");*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //add by mx_wj
    public static URL getJarURL(Class<?> clazz) throws UnsupportedEncodingException, NullPointerException, MalformedURLException{
        String file = "";
        CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
        URL url = codeSource.getLocation();
        file = url.getPath();
        if(!file.isEmpty()){
            if(file.endsWith(".class") && file.contains("!"))
                file = file.substring(0, file.lastIndexOf("!"));
            if (file.startsWith("file:"))
                file = file.substring(5); 
            if (file.startsWith("/"))
                file = file.substring(1); 
        }
        return new File(URLDecoder.decode(file, "UTF-8")).toURL();
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
