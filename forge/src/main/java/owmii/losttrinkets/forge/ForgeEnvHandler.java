package owmii.losttrinkets.forge;

import java.util.Collection;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.WorldAccess;
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
        NbtCompound persistentData = entity.getPersistentData();
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
        ThreadedAnvilChunkStorage chunkManager = ((ServerChunkManager)entity.getEntityWorld().getChunkManager()).threadedAnvilChunkStorage;
        return ((ThreadedAnvilChunkStorageTrackingExtension) chunkManager).forge_getTrackingPlayers(entity);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, PlayerEntity player, WorldAccess world, BlockPos pos) {
        return ForgeHooks.isCorrectToolForDrops(state, player);
    }

    @Override
    public boolean isOreBlock(Block block) {
        return Tags.Blocks.ORES.contains(block);
    }

    @Override
    public void teleport(PlayerEntity player, ServerWorld world, TeleportTarget target) {
        player.changeDimension(world, new ITeleporter() {
            @Override
            @Nullable
            public TeleportTarget getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, TeleportTarget> defaultPortalInfo) {
                return target;
            }
        });
    }

    @Override
    public boolean shouldRiderSit(Entity entity) {
        return entity.shouldRiderSit();
    }
}
