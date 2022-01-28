package ultimate.common.util;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCombatEvent;
import ultimate.common.item.ItemUltimateSword;
import ultimate.common.registry.UltimateItemLoader;

public final class UltimateUtil {

    private static final Set<String> ULTIMATE_PLAYERS = Sets.newHashSet();

    private UltimateUtil() {
    }

    public static int getUltimateDeathTime(Entity entity) {
        if (entity == null) {
            return 0;
        }

        return entity.getEntityData().getInteger("UltimateDeathTime");
    }

    public static int increaseUltimateDeathTime(Entity entity) {
        NBTTagCompound entityData = entity.getEntityData();
        int deathTime = entityData.getInteger("UltimateDeathTime");
        entityData.setInteger("UltimateDeathTime", ++deathTime);
        return deathTime;
    }

    public static void kill(Entity entity) {
        entity.getEntityData().setBoolean("UltimateDead", true);
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP serverPlayer = (EntityPlayerMP) entity;
            serverPlayer.connection.sendPacket(
                    new SPacketCombatEvent(serverPlayer.getCombatTracker(), SPacketCombatEvent.Event.ENTITY_DIED));
        }
    }

    public static boolean isUltimateDead(Entity entity) {
        return entity.getEntityData().getBoolean("UltimateDead");
    }

    public static boolean isUltimatePlayer(Object object) {
        if (object instanceof EntityPlayer) {
            return isUltimatePlayer((EntityPlayer) object);
        }

        return false;
    }

    public static boolean isUltimatePlayer(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return isUltimatePlayer((EntityPlayer) entity);
        }

        return false;
    }

    public static boolean isUltimatePlayer(EntityPlayer player) {
        if (player == null) {
            return false;
        }

        if (ULTIMATE_PLAYERS.contains(player.getName())) {
            return true;
        } else if (inventoryHasUltimate(player)) {
            addUltimatePlayer(player);
            return true;
        }

        return false;
    }

    public static boolean inventoryHasUltimate(EntityPlayer player) {
        if (player.getHeldItemMainhand().getItem() instanceof ItemUltimateSword
                || player.getHeldItemOffhand().getItem() instanceof ItemUltimateSword) {
            return true;
        }

        if (player.inventory.hasItemStack(new ItemStack(UltimateItemLoader.ITEM_ULTIMATE_SWORD))) {
            return true;
        }

        return false;
    }

    public static void addUltimatePlayer(EntityPlayer player) {
        ULTIMATE_PLAYERS.add(player.getName());
    }
}
