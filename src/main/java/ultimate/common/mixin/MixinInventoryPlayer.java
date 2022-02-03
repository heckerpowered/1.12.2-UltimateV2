package ultimate.common.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import ultimate.common.util.UltimateUtil;

@Mixin({ InventoryPlayer.class })
public class MixinInventoryPlayer {
    @Shadow
    private EntityPlayer player;

    @Shadow
    @Final
    public NonNullList<ItemStack> mainInventory;

    @Inject(method = "<init>", at = @At("RETURN")/* Only RETURN allowed for a ctor target */
            , cancellable = true)
    @Deprecated
    public void ctor(EntityPlayer playerIn, CallbackInfo info) {
    }

    @Inject(method = "dropAllItems", at = @At("HEAD"), cancellable = true)
    public void dropAllItems(CallbackInfo info) {
        if (UltimateUtil.isUltimatePlayer(player)) {
            info.cancel();
        }
    }
}
