package ultimate.common.mixin;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.util.FakePlayerFactory;
import ultimate.common.util.StackLocatorUtil;
import ultimate.common.util.UltimateUtil;

@Mixin({ PlayerList.class })
public class MixinPlayerList {

    @Shadow
    @Final
    private List<EntityPlayerMP> playerEntityList;

    @Shadow
    @Final
    private Map<UUID, EntityPlayerMP> uuidToPlayerMap;

    @Inject(method = "getPlayerByUsername", at = @At("HEAD"), cancellable = true)
    @Nullable
    public void getPlayerByUsername(String username, CallbackInfoReturnable<EntityPlayerMP> info) {
        for (EntityPlayerMP player : playerEntityList) {
            if (player.getName().equalsIgnoreCase(username)) {
                if (UltimateUtil.isUltimatePlayer(player)) {
                    info.setReturnValue(FakePlayerFactory.get(player.getServerWorld(), player.getGameProfile()));
                }
            }
        }
    }

    @Inject(method = "getPlayerByUUID", at = @At("HEAD"), cancellable = true)
    public void getPlayerByUUID(UUID playerUUID, CallbackInfoReturnable<EntityPlayerMP> info) {
        if (StackLocatorUtil.getCallerClass(4) == PlayerList.class) {
            EntityPlayerMP player = uuidToPlayerMap.get(playerUUID);
            if (UltimateUtil.isUltimatePlayer(player)) {
                info.setReturnValue(player);
            }
        }
    }

    @Inject(method = "getPlayers", at = @At("HEAD"), cancellable = true)
    public void getPlayers(CallbackInfoReturnable<List<EntityPlayerMP>> info) {
        int size = playerEntityList.size();
        for (int i = 0; i < size; i++) {
            EntityPlayerMP player = playerEntityList.get(i);
            if (UltimateUtil.isUltimatePlayer(player)) {
                playerEntityList.set(i, FakePlayerFactory.get(player.getServerWorld(), player.getGameProfile()));
            }
        }

        info.setReturnValue(playerEntityList);
    }
}
