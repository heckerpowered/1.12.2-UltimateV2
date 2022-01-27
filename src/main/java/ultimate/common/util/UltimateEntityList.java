package ultimate.common.util;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import ultimate.UltimateMod;

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

    @Override
    public boolean add(Entity e) {
        if (UltimateUtil.isUltimateDead(e)) {
            return false;
        }

        return super.add(e);
    }

    @Override
    public void add(int index, Entity element) {
        if (UltimateUtil.isUltimateDead(element)) {
            return;
        }

        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends Entity> c) {
        c.removeIf(UltimateUtil::isUltimateDead);
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Entity> c) {
        c.removeIf(UltimateUtil::isUltimateDead);
        return super.addAll(index, c);
    }

    @Override
    public Entity get(int index) {
        Entity entity = super.get(index);
        if (UltimateUtil.isUltimatePlayer(entity)) {
            Class<?> callerClass = StackLocatorUtil.getCallerClass(4);
            if (callerClass != World.class) {
                UltimateMod.getLogger().fatal("Invalid call from {} detected.", callerClass);
                if (entity.world instanceof WorldServer) {
                    return FakePlayerFactory.get((WorldServer) entity.world, ((EntityPlayer) entity).getGameProfile());
                }
            }
        }
        return entity;
    }
}
