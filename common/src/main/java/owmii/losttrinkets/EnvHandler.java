package owmii.losttrinkets;

import java.util.Collection;

import me.shedaniel.architectury.platform.Platform;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

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

    MinecraftServer getServerInstance();

    LootTable getLootTableFromLocation(ResourceLocation location);

    boolean magnetCanCollect(Entity entity, boolean automated);

    Collection<ServerPlayerEntity> getTrackingPlayers(Entity entity);

    boolean canHarvestBlock(BlockState state, PlayerEntity player, IWorld world, BlockPos pos);

    boolean isOreBlock(Block block);

    void teleport(PlayerEntity player, ServerWorld world, PortalInfo target);

    boolean isFakePlayer(PlayerEntity player);

    default boolean shouldRiderSit(Entity entity) {
        return true;
    }
}
