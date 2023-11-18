package owmii.losttrinkets.lib.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

@SuppressWarnings("resource")
public class MC {
    public static Optional<PlayerEntity> player() {
        return Optional.ofNullable(get().player);
    }

    public static Optional<World> world() {
        return Optional.ofNullable(get().world);
    }

    public static MinecraftClient get() {
        return MinecraftClient.getInstance();
    }
}
