package owmii.losttrinkets.lib.util;

import java.util.Optional;
import java.util.UUID;

import me.shedaniel.architectury.utils.GameInstance;
import net.minecraft.entity.player.ServerPlayerEntity;

public class Server {
    public static Optional<ServerPlayerEntity> getPlayerByUUID(UUID uuid) {
        return Optional.ofNullable(GameInstance.getServer().getPlayerList().getPlayerByUUID(uuid));
    }
}