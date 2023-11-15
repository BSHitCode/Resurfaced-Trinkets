package owmii.losttrinkets.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.network.IPacket;

import java.util.List;

public class SetInactivePacket implements IPacket {
    private int trinket;

    public SetInactivePacket(int trinket) {
        this.trinket = trinket;
    }

    public SetInactivePacket() {
        this(0);
    }

    public SetInactivePacket(PacketBuffer buffer) {
        this(buffer.readInt());
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.trinket);
    }

    @Override
    public void handle(PlayerEntity player) {
        if (player != null) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            List<ITrinket> items = trinkets.getActiveTrinkets();
            if (!items.isEmpty()) {
                trinkets.setInactive(items.get(this.trinket), player);
            }
        }
    }
}