package owmii.losttrinkets.lib.util;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import owmii.losttrinkets.EnvHandler;

public class Server {
    @Deprecated
    public static MinecraftServer get() {
        return EnvHandler.INSTANCE.getServerInstance();
    }

    public static Optional<ServerPlayerEntity> getPlayerByUUID(UUID uuid) {
        return Optional.ofNullable(Server.get().getPlayerList().getPlayerByUUID(uuid));
    }
}