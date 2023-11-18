package owmii.losttrinkets.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.client.screen.Screens;
import owmii.losttrinkets.lib.client.util.MC;
import owmii.losttrinkets.network.IPacket;

import java.util.Objects;
import java.util.UUID;

public class SyncDataPacket implements IPacket {
    private final UUID uuid;
    private final NbtCompound nbt;

    protected SyncDataPacket(UUID uuid, NbtCompound nbt) {
        this.uuid = uuid;
        this.nbt = nbt;
    }

    public SyncDataPacket() {
        this(new UUID(0, 0), new NbtCompound());
    }

    public SyncDataPacket(PlayerEntity player) {
        this(player.getUuid(), LostTrinketsAPI.getData(player).serializeNBT());
    }

    public SyncDataPacket(PacketByteBuf buffer) {
        this(buffer.readUuid(), Objects.requireNonNull(buffer.readNbt()));
    }

    @Override
    public void encode(PacketByteBuf buffer) {
        buffer.writeUuid(this.uuid);
        buffer.writeNbt(this.nbt);
    }

    @Override
    public void handle(PlayerEntity sender) {
        MC.world().ifPresent(world -> {
            PlayerEntity player = world.getPlayerByUuid(this.uuid);
            if (player != null) {
                PlayerData data = LostTrinketsAPI.getData(player);
                data.deserializeNBT(this.nbt);
                Screens.checkScreenRefresh();
            }
        });
    }
}
