package owmii.losttrinkets.fabric;

import java.util.Collection;

import me.shedaniel.architectury.hooks.TagHooks;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import owmii.losttrinkets.EnvHandler;

public class FabricEnvHandler implements EnvHandler {
    static final ITag.INamedTag<Block> ORES = TagHooks.getBlockOptional(new ResourceLocation("c", "ores"));

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
    public boolean canHarvestBlock(BlockState state, PlayerEntity player, IWorld world, BlockPos pos) {
        // TODO: figure this out
        return true;
    }

    @Override
    public boolean isOreBlock(Block block) {
        // TODO: switch to fabric-convention-tags-v1 in 1.18
        return ORES.contains(block);
    }

    @Override
    public void teleport(PlayerEntity player, ServerWorld world, PortalInfo target) {
        FabricDimensions.teleport(player, world, target);
    }
}
