package ultimate.common.util;

import java.util.Collection;
import java.util.HashSet;

import net.minecraft.entity.Entity;

public final class UltimateEntitySet extends HashSet<Entity> {
    public UltimateEntitySet(Collection<Entity> c) {
        super(c);
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof Entity) {
            if (UltimateUtil.isUltimatePlayer((Entity) o)) {
                return false;
            }
        }
        return super.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.removeIf(UltimateUtil::isUltimatePlayer);
        return super.removeAll(c);
    }
}
