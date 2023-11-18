package owmii.losttrinkets.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.network.IPacket;

import java.util.List;

public class SetActivePacket implements IPacket {
    private int trinket;

    public SetActivePacket(int trinket) {
        this.trinket = trinket;
    }

    public SetActivePacket() {
        this(0);
    }

    public SetActivePacket(PacketByteBuf buffer) {
        this(buffer.readInt());
    }

    @Override
    public void encode(PacketByteBuf buffer) {
        buffer.writeInt(this.trinket);
    }

    @Override
    public void handle(PlayerEntity player) {
        if (player != null) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            List<ITrinket> items = trinkets.getAvailableTrinkets();
            if (!items.isEmpty()) {
                trinkets.setActive(items.get(this.trinket), player);
            }
        }
    }
}