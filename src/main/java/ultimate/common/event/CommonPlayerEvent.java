package ultimate.common.event;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import ultimate.common.util.UltimateUtil;

public class CommonPlayerEvent {
    @EventHandler
    public static void onLivingAttack(LivingAttackEvent event) {
        if (UltimateUtil.isUltimatePlayer(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @EventHandler
    public static void onLivingHurt(LivingHurtEvent event) {
        if (UltimateUtil.isUltimatePlayer(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @EventHandler
    public static void onLivingFall(LivingFallEvent event) {
        if (UltimateUtil.isUltimatePlayer(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @EventHandler
    public static void onLivingDeath(LivingDeathEvent event) {
        if (UltimateUtil.isUltimatePlayer(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @EventHandler
    public static void onLivingDamage(LivingDamageEvent event) {
        if (UltimateUtil.isUltimatePlayer(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @EventHandler
    public static void onLivingKnockback(LivingKnockBackEvent event) {
        if (UltimateUtil.isUltimatePlayer(event.getEntity())) {
            event.setCanceled(true);
        }
    }
}
