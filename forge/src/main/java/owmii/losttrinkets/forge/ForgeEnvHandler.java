package owmii.losttrinkets.forge;

import java.util.Collection;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.ITeleporter;
import owmii.losttrinkets.EnvHandler;
import owmii.losttrinkets.lib.compat.botania.BotaniaCompat;

public class ForgeEnvHandler implements EnvHandler {
    public static final String PREVENT_REMOTE_MOVEMENT = "PreventRemoteMovement";
    public static final String ALLOW_MACHINE_REMOTE_MOVEMENT = "AllowMachineRemoteMovement";

    /**
     * Checks if the entity can be collected by a magnet, vacuum or similar.
     *
     * @param entity    The Entity in question
     * @param automated true if the magnet does not require a player to operate
     * @see <a href="https://github.com/comp500/Demagnetize#for-mod-developers">Demagnetize: For mod developers</a>
     */
    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean magnetCanCollect(Entity entity, boolean automated) {
        // Demagnetize standard
        CompoundNBT persistentData = entity.getPersistentData();
        if (persistentData.contains(PREVENT_REMOTE_MOVEMENT)) {
            if (!(automated && persistentData.contains(ALLOW_MACHINE_REMOTE_MOVEMENT))) {
                return false;
            }
        }

        if (BotaniaCompat.preventCollect(entity)) {
            return false;
        }

        return true;
    }

    @Override
    public Collection<ServerPlayerEntity> getTrackingPlayers(Entity entity) {
        ChunkManager chunkManager = ((ServerChunkProvider)entity.getEntityWorld().getChunkProvider()).chunkManager;
        return ((ChunkManagerTrackingExtension) chunkManager).forge_getTrackingPlayers(entity);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, PlayerEntity player, IWorld world, BlockPos pos) {
        return ForgeHooks.canHarvestBlock(state, player, world, pos);
    }

    @Override
    public boolean isOreBlock(Block block) {
        return Tags.Blocks.ORES.contains(block);
    }

    @Override
    public void teleport(PlayerEntity player, ServerWorld world, PortalInfo target) {
        player.changeDimension(world, new ITeleporter() {
            @Override
            @Nullable
            public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
                return target;
            }
        });
    }

    @Override
    public boolean shouldRiderSit(Entity entity) {
        return entity.shouldRiderSit();
    }
}
