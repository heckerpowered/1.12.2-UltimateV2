package ultimate.common.util;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.entity.Entity;

public final class UltimateEntityList extends ArrayList<Entity> {
    public UltimateEntityList(Collection<Entity> c) {
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

    @Override
    public Entity remove(int index) {
        Entity entity = super.get(index);
        if (UltimateUtil.isUltimatePlayer(entity)) {
            return entity;
        }

        return super.remove(index);
    }
}
