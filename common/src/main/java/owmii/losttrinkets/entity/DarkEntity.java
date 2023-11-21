package owmii.losttrinkets.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import owmii.losttrinkets.lib.util.Server;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DarkEntity extends PathAwareEntity {
    @Nullable
    protected UUID owner;
    @Nullable
    protected PlayerEntity player;

    public DarkEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient) {
            if (this.owner == null) {
                vanish();
            } else {
                Optional<ServerPlayerEntity> player = Server.getPlayerByUUID(this.owner);
                if (player.isPresent()) {
                    this.player = player.get();
                } else vanish();
            }
            if (getTarget() == null || !getTarget().isAlive()) {
                List<MobEntity> entities = this.world.getNonSpectatingEntities(MobEntity.class, new Box(getBlockPos()).expand(24));
                boolean flag = false;
                for (MobEntity entity : entities) {
                    if (entity.getTarget() != null) {
                        if (this.owner.equals(entity.getTarget().getUuid())) {
                            setTarget(entity);
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) vanish();
            }
        }
    }

    protected void vanish() {
        playSpawnEffects();
        remove(RemovalReason.DISCARDED);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        if (compound.contains("owner")) {
            this.owner = compound.getUuid("owner");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        if (this.owner != null) {
            compound.putUuid("owner", this.owner);
        }
    }

    @Nullable
    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(PlayerEntity owner) {
        this.owner = owner.getUuid();
    }
}
