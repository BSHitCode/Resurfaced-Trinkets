package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class BlazeHeartTrinket extends Trinket<BlazeHeartTrinket> {
    public BlazeHeartTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static boolean isImmuneToFire(LivingEntity target, DamageSource source) {
        if (target instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) target;
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.BLAZE_HEART)) {
                if (source.isFire()) {
                    player.extinguish();
                    return true;
                }
            }
        }
        return false;
    }
}
