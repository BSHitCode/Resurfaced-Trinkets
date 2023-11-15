package owmii.losttrinkets.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.network.IPacket;

public class UnlockSlotPacket implements IPacket {
    public UnlockSlotPacket() {}

    public UnlockSlotPacket(PacketBuffer buffer) {}

    @Override
    public void encode(PacketBuffer buffer) {
    }

    @Override
    public void handle(PlayerEntity player) {
        if (player != null) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            int cost = LostTrinkets.config().calcCost(trinkets.getSlots());
            if (cost >= 0) {
                if (player.isCreative()) {
                    trinkets.unlockSlot();
                } else if (player.experienceLevel >= cost) {
                    if (trinkets.unlockSlot()) {
                        player.addExperienceLevel(-cost);
                    }
                }
            }
        }
    }
}