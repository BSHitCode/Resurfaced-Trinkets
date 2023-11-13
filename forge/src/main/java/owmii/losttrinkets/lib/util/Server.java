package owmii.losttrinkets.lib.util;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class Server {
    public static MinecraftServer get() {
        return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
    }

    public static Optional<ServerPlayerEntity> getPlayerByUUID(UUID uuid) {
        return Optional.ofNullable(Server.get().getPlayerList().getPlayerByUUID(uuid));
    }
}