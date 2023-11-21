package owmii.losttrinkets.fabric;

import java.util.Collection;

import dev.architectury.hooks.tags.TagHooks;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.WorldAccess;
import owmii.losttrinkets.EnvHandler;

public class FabricEnvHandler implements EnvHandler {
    static final Tag.Identified<Block> ORES = TagHooks.optionalBlock(new Identifier("c", "ores"));

    @Override
    public boolean magnetCanCollect(Entity entity, boolean automated) {
        // TODO: port from forge
        return true;
    }

    @Override
    public Collection<ServerPlayerEntity> getTrackingPlayers(Entity entity) {
        return PlayerLookup.tracking(entity);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, PlayerEntity player, WorldAccess world, BlockPos pos) {
        // TODO: figure this out
        return true;
    }

    @Override
    public boolean isOreBlock(Block block) {
        // TODO: switch to fabric-convention-tags-v1 in 1.18
        return ORES.contains(block);
    }

    @Override
    public void teleport(PlayerEntity player, ServerWorld world, TeleportTarget target) {
        FabricDimensions.teleport(player, world, target);
    }
}
