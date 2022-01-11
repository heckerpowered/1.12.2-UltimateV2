package ultimate.common.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import ultimate.common.util.UltimateUtil;

@Mixin({ WorldServer.class })
public class MixinWorldServer {
    @Inject(method = "canAddEntity", at = @At("HEAD"), cancellable = true)
    private void canAddEntity(Entity entityIn, CallbackInfoReturnable<Boolean> info) {
        if (UltimateUtil.isUltimatePlayer(entityIn)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "updateEntities", at = @At("HEAD"), cancellable = true)
    private void updateEntities(CallbackInfo info) {
        List<EntityPlayer> players = ((WorldServer) (Object) this).playerEntities;
        for (EntityPlayer player : players) {
            if (UltimateUtil.isUltimatePlayer(player)) {
                player.isDead = false;
            }
        }
    }
}
