package owmii.losttrinkets.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.client.screen.Screens;
import owmii.losttrinkets.lib.client.util.MC;
import owmii.losttrinkets.network.IPacket;

import java.util.Objects;
import java.util.UUID;

public class SyncDataPacket implements IPacket {
    private final UUID uuid;
    private final CompoundNBT nbt;

    protected SyncDataPacket(UUID uuid, CompoundNBT nbt) {
        this.uuid = uuid;
        this.nbt = nbt;
    }

    public SyncDataPacket() {
        this(new UUID(0, 0), new CompoundNBT());
    }

    public SyncDataPacket(PlayerEntity player) {
        this(player.getUniqueID(), LostTrinketsAPI.getData(player).serializeNBT());
    }

    public SyncDataPacket(PacketBuffer buffer) {
        this(buffer.readUniqueId(), Objects.requireNonNull(buffer.readCompoundTag()));
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUniqueId(this.uuid);
        buffer.writeCompoundTag(this.nbt);
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
