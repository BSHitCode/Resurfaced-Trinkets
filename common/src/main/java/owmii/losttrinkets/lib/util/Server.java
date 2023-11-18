package owmii.losttrinkets.lib.util;

import java.util.Optional;
import java.util.UUID;

import me.shedaniel.architectury.utils.GameInstance;
import net.minecraft.server.network.ServerPlayerEntity;

public class Server {
    public static Optional<ServerPlayerEntity> getPlayerByUUID(UUID uuid) {
        return Optional.ofNullable(GameInstance.getServer().getPlayerManager().getPlayer(uuid));
    }
}