package owmii.losttrinkets;

import java.util.Collection;

import me.shedaniel.architectury.platform.Platform;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public interface EnvHandler {
    EnvHandler INSTANCE = Util.make(() -> {
        try {
            Class<?> klass = Class.forName(
                Platform.isForge() ? "owmii.losttrinkets.forge.ForgeEnvHandler" : "owmii.losttrinkets.fabric.FabricEnvHandler"
            );
            return (EnvHandler) klass.getConstructor().newInstance();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to setup env handler", exception);
        }
    });
}
