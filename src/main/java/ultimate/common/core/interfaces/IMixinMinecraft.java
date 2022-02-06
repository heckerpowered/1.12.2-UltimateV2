package ultimate.common.core.interfaces;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IMixinMinecraft {
    public float getPausedPartialTicks();
}
