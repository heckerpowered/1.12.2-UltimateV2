package ultimate.common.util;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ultimate.UltimateMod;
import ultimate.common.core.interfaces.IMixinEntity;
import ultimate.common.item.ItemUltimateSword;
import ultimate.common.network.PacketHandler;
import ultimate.common.network.PacketRemoveObject.MessageRemoveObject;
import ultimate.common.network.PacketSyncUltimatePlayer.MessageSyncUltimatePlayer;
import ultimate.common.registry.UltimateItemLoader;

public final class UltimateUtil {

    private static final Set<String> ULTIMATE_PLAYERS = Sets.newHashSet();

    private UltimateUtil() {
    }

    public static int getUltimateDeathTime(@Nullable Entity entity) {
        if (entity == null) {
            return 0;
        }

        return ((IMixinEntity) entity).getDeathTime();
    }

    public static int increaseUltimateDeathTime(@Nullable Entity entity) {
        if (entity == null) {
            return 0;
        }

        IMixinEntity mixinEntity = ((IMixinEntity) entity);
        int deathTime = mixinEntity.getDeathTime();
        mixinEntity.setDeathTime(++deathTime);
        return deathTime;
    }

    public static void kill(Entity entity) {
        if (entity == null) {
            return;
        }

        killInternal(entity);
        if (entity instanceof IEntityMultiPart) {
            Entity[] parts = entity.getParts();
            for (Entity part : parts) {
                killInternal(part);
            }
        }
    }

    private static void killInternal(Entity entity) {
        ((IMixinEntity) entity).setUltimateDead();
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP serverPlayer = (EntityPlayerMP) entity;
            PacketHandler.sendTo(new MessageRemoveObject(), serverPlayer);
        }
    }

    public static boolean isUltimateDead(@Nullable Entity entity) {
        if (entity == null) {
            return false;
        }

        return ((IMixinEntity) entity).isUltimateDead();
    }

    public static boolean isUltimatePlayer(Object object) {
        if (object instanceof EntityPlayer) {
            return isUltimatePlayer((EntityPlayer) object);
        } else if (object instanceof String) {
            return isUltimatePlayer((String) object);
        }

        return false;
    }

    private static boolean isUltimatePlayer(String string) {
        return ULTIMATE_PLAYERS.contains(string) || UltimateMod.proxy.isUltimatePlayer(string);
    }

    public static boolean isUltimatePlayer(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return isUltimatePlayer((EntityPlayer) entity);
        }

        return false;
    }

    public static boolean isUltimatePlayer(EntityPlayer player) {
        try {
            if (isUltimatePlayer(player.getGameProfile().getName())) {
                return true;
            } else if (inventoryHasUltimate(player)) {
                addUltimatePlayer(player);
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    public static boolean inventoryHasUltimate(@Nullable EntityPlayer player) {
        if (player == null) {
            return false;
        }

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
        String name = player.getName();
        addUltimatePlayer(name);
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            PacketHandler.sendToAll(new MessageSyncUltimatePlayer(name));
        }
    }

    public static void addUltimatePlayer(String player) {
        ULTIMATE_PLAYERS.add(player);
    }
}
