package owmii.losttrinkets.item.trinkets;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.SpawnPointCommand;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import owmii.losttrinkets.EnvHandler;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

import java.util.Optional;
import java.util.function.Consumer;

public class WarmVoidTrinket extends Trinket<WarmVoidTrinket> implements ITickableTrinket {
    public WarmVoidTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(World world, BlockPos pos, PlayerEntity player) {
        // TODO 1.18: Update -64
        if (player instanceof ServerPlayerEntity && player.getY() + Math.min(0, player.getVelocity().getY()) <= -64) {
            if (!player.hasVehicle() && !player.hasPassengers()) {
                teleportToSpawnPoint((ServerPlayerEntity) player);
            }
        }
    }

    /**
     * Follow code in {@link SpawnPointCommand} to find the real spawn point information.
     */
    private static void teleportToSpawnPoint(ServerPlayerEntity player) {
        player.stopRiding();
        SpawnPointInfo info = getSpawnPointInfo(player);
        player.setVelocity(Vec3d.ZERO);
        player.fallDistance = 0;
        if (player.getServerWorld() != info.spawnWorld) {
            // player.changeDimension(info.spawnWorld, new WarmVoidTeleporter(info));
            EnvHandler.INSTANCE.teleport(player, info.spawnWorld, new TeleportTarget(info.spawnPos, Vec3d.ZERO, info.spawnAngle, 0));
        } else {
            player.networkHandler.requestTeleport(info.spawnPos.getX(), info.spawnPos.getY(), info.spawnPos.getZ(), info.spawnAngle, 0);

            // TODO: find out if this is neccessary...
            // info.repositionEntity.accept(player);
        }
    }

    /**
     * Follow code in {@link ServerPlayNetworkHandler#onClientStatus(ClientStatusC2SPacket)}.
     * Based on {@link PlayerManager#respawnPlayer(ServerPlayerEntity, boolean)}.
     */
    private static SpawnPointInfo getSpawnPointInfo(ServerPlayerEntity player) {
        MinecraftServer server = player.getServerWorld().getServer();
        BlockPos spawnPosRaw = player.getSpawnPointPosition();
        float spawnAngle = player.getSpawnAngle();
        boolean spawnForced = player.isSpawnPointSet();
        ServerWorld spawnWorldRaw = server.getWorld(player.getSpawnPointDimension());
        Optional<Vec3d> spawnPos;
        if (spawnWorldRaw != null && spawnPosRaw != null) {
            spawnPos = PlayerEntity.findRespawnPosition(spawnWorldRaw, spawnPosRaw, spawnAngle, spawnForced, true);
        } else {
            spawnPos = Optional.empty();
        }

        ServerWorld spawnWorld = spawnWorldRaw != null && spawnPos.isPresent() ? spawnWorldRaw : server.getOverworld();

        Consumer<ServerPlayerEntity> repositionEntity = callbackPlayer -> {
            if (spawnPos.isPresent()) {
                BlockState blockstate = spawnWorld.getBlockState(spawnPosRaw);
                boolean isRespawnAnchor = blockstate.isOf(Blocks.RESPAWN_ANCHOR);
                Vec3d spawnPosResolved = spawnPos.get();
                float spawnAngleResolved;
                if (!blockstate.isIn(BlockTags.BEDS) && !isRespawnAnchor) {
                    spawnAngleResolved = spawnAngle;
                } else {
                    Vec3d vector3d1 = Vec3d.ofBottomCenter(spawnPosRaw).subtract(spawnPosResolved).normalize();
                    spawnAngleResolved = (float) MathHelper.wrapDegrees(MathHelper.atan2(vector3d1.z, vector3d1.x) * (double) (180F / (float) Math.PI) - 90.0D);
                }

                callbackPlayer.refreshPositionAndAngles(spawnPosResolved.x, spawnPosResolved.y, spawnPosResolved.z, spawnAngleResolved, 0.0F);
                callbackPlayer.setSpawnPoint(spawnWorld.getRegistryKey(), spawnPosRaw, spawnAngle, spawnForced, false);
            } else if (spawnPosRaw != null) {
                callbackPlayer.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.NO_RESPAWN_BLOCK, 0.0F));
            }

            while (!spawnWorld.isSpaceEmpty(callbackPlayer) && callbackPlayer.getY() < 256.0D) {
                callbackPlayer.setPosition(callbackPlayer.getX(), callbackPlayer.getY() + 1.0D, callbackPlayer.getZ());
            }
        };
        return new SpawnPointInfo(
                spawnWorld,
                spawnPos.orElseGet(() -> Vec3d.ofBottomCenter(spawnWorld.getSpawnPos())),
                spawnAngle,
                repositionEntity
        );
    }

    /* private static class WarmVoidTeleporter implements ITeleporter {
        private final SpawnPointInfo info;

        public WarmVoidTeleporter(SpawnPointInfo info) {
            this.info = info;
        }

        @Override
        public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
            entity = repositionEntity.apply(false);
            if (entity instanceof ServerPlayerEntity) {
                info.repositionEntity.accept((ServerPlayerEntity) entity);
            }
            entity.setPositionAndUpdate(entity.getPosX(), entity.getPosY(), entity.getPosZ());
            return entity;
        }

        @Nullable
        @Override
        public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
            return new PortalInfo(info.spawnPos, Vector3d.ZERO, info.spawnAngle, 0);
        }
    } */

    private static class SpawnPointInfo {
        public final ServerWorld spawnWorld;
        public final Vec3d spawnPos;
        public final float spawnAngle;
        @SuppressWarnings("unused")
        public final Consumer<ServerPlayerEntity> repositionEntity;

        public SpawnPointInfo(ServerWorld spawnWorld, Vec3d spawnPos, float spawnAngle, Consumer<ServerPlayerEntity> repositionEntity) {
            this.spawnWorld = spawnWorld;
            this.spawnPos = spawnPos;
            this.spawnAngle = spawnAngle;
            this.repositionEntity = repositionEntity;
        }
    }
}
