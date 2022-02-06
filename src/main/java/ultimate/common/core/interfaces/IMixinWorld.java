package ultimate.common.core.interfaces;

import net.minecraft.entity.Entity;

public interface IMixinWorld {
    public void fastRemove(Entity e);

    public void setTheWorld(boolean theWorld);

    public boolean isTheWorld();
}
