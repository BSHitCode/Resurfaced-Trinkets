package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class MinersPickTrinket extends Trinket<MinersPickTrinket> {
    public MinersPickTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static float onBreakSpeed(PlayerEntity player, float originalSpeed) {
        if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.MINERS_PICK)) {
            return originalSpeed + 3.7F;
        }
        return originalSpeed;
    }
}
