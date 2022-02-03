package ultimate.common.entity;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public final class UltimateFakePlayer extends FakePlayer {
    // Map of all active fake player usernames to their entities
    private static Map<GameProfile, UltimateFakePlayer> fakePlayers = Maps.newHashMap();

    public UltimateFakePlayer(WorldServer world, GameProfile name) {
        super(world, name);
    }

    @Override
    public void closeScreen() {

    }

    public static UltimateFakePlayer getFakePlayer(EntityPlayerMP player) {
        GameProfile profile = player.getGameProfile();
        if (fakePlayers.containsKey(profile)) {
            return fakePlayers.get(profile);
        }

        UltimateFakePlayer fakePlayer = new UltimateFakePlayer(player.getServerWorld(), profile);
        fakePlayers.put(profile, fakePlayer);
        return fakePlayer;
    }
}
