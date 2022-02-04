package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import ultimate.common.item.ItemUltimateSword;
import ultimate.common.registry.UltimateItemLoader;
import ultimate.common.util.UltimateUtil;

@Mixin({ EntityPlayer.class })
public class MixinEntityPlayer {
    private EntityPlayer player = (EntityPlayer) (Object) this;

    @Inject(method = "onDeath", cancellable = true, at = @At("HEAD"))
    public void onDeath(DamageSource cause, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.cancel();
        }
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onUpdate(CallbackInfo info) {
        if (UltimateUtil.inventoryHasUltimate(player)) {
            UltimateUtil.addUltimatePlayer(player);
        }

        if (UltimateUtil.isUltimatePlayer(player)) {
            player.getFoodStats().setFoodLevel(20);
            player.isDead = false;
            player.deathTime = 0;
            player.hurtTime = 0;
            if (!UltimateUtil.inventoryHasUltimate(player)) {
                player.inventory.addItemStackToInventory(new ItemStack(UltimateItemLoader.ITEM_ULTIMATE_SWORD));
            }
        }
    }

    @Inject(method = "dropItem(Z)Lnet/minecraft/entity/item/EntityItem;", at = @At("HEAD"), cancellable = true)
    public void dropItem(boolean dropAll, CallbackInfoReturnable<EntityItem> info) {
        if (UltimateUtil.isUltimatePlayer(player)
                && !(player.inventory.getCurrentItem().getItem() instanceof ItemUltimateSword)) {
            info.setReturnValue(null);
        }
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/item/EntityItem;", at = @At("HEAD"), cancellable = true)
    public void dropItem(ItemStack itemStackIn, boolean unused, CallbackInfoReturnable<EntityItem> info) {
        if (itemStackIn.getItem() instanceof ItemUltimateSword) {
            info.setReturnValue(null);
        }
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;", at = @At("HEAD"), cancellable = true)

    public void dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem,
            CallbackInfoReturnable<EntityItem> info) {
        if (droppedItem.getItem() instanceof ItemUltimateSword) {
            info.setReturnValue(null);
        }
    }

    @Inject(method = "canAttackPlayer", at = @At("HEAD"), cancellable = true)
    public void canAttackPlayer(EntityPlayer other, CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(other)) {
            info.setReturnValue(false);
        } else if (UltimateUtil.isUltimatePlayer(player)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "setDead", at = @At("HEAD"), cancellable = true)
    public void setDead(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.cancel();
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    public void getHurtSound(DamageSource damageSourceIn, CallbackInfoReturnable<SoundEvent> info) {
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.setReturnValue(null);
        }
    }

    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    public void getDeathSound(CallbackInfoReturnable<SoundEvent> info) {
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.setReturnValue(null);
        }
    }

    @Inject(method = "isMovementBlocked", cancellable = true, at = @At("HEAD"))
    public void isMovementBlocked(CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "collideWithPlayer", cancellable = true, at = @At("HEAD"))
    public void collideWithPlayer(Entity entityIn, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(player) || UltimateUtil.isUltimatePlayer(entityIn)) {
            info.cancel();
        }
    }

    @Inject(method = "fall", cancellable = true, at = @At("HEAD"))
    public void fall(float distance, float damageMultiplier, CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.cancel();
        }
    }
}
