package ultimate.common.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
        } else if (UltimateUtil.isUltimateDead(entityIn)) {
            info.setReturnValue(false);
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

    @Redirect(method = "tickPlayers", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;isDead:Z"))
    private boolean tickPlayers(Entity entity) {
        if (UltimateUtil.isUltimatePlayer(entity)) {
            return false;
        }

        return entity.isDead;
    }
}
