package owmii.losttrinkets.lib.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class MC {
    public static Optional<PlayerEntity> player() {
        return Optional.ofNullable(get().player);
    }

    public static Optional<World> world() {
        return Optional.ofNullable(get().world);
    }

    public static Minecraft get() {
        return Minecraft.getInstance();
    }
}
