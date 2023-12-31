package sun;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class UltimateMixinLoader {
    public static void initMixin() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.ultimate.json");
    }
}
