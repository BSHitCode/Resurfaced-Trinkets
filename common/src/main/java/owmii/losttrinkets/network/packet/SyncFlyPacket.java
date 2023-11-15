package owmii.losttrinkets.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.lib.client.util.MC;
import owmii.losttrinkets.network.IPacket;

public class SyncFlyPacket implements IPacket {
    private boolean fly;

    public SyncFlyPacket(boolean fly) {
        this.fly = fly;
    }

    public SyncFlyPacket() {
        this(false);
    }

    public SyncFlyPacket(PacketBuffer buffer) {
        this(buffer.readBoolean());
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(this.fly);
    }

    @Override
    public void handle(PlayerEntity _sender) {
        MC.player().ifPresent(player -> {
            PlayerData data = LostTrinketsAPI.getData(player);
            data.allowFlying = this.fly;
            player.abilities.allowFlying = this.fly;
            if (!this.fly) {
                player.abilities.isFlying = false;
            }
        });
    }
}
