package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class CoffeeBeanTrinket extends Trinket<CoffeeBeanTrinket> {
    public CoffeeBeanTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onPotion(LivingEntity entity, Effect effect, Runnable denyResult) {
        if (entity instanceof PlayerEntity) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets((PlayerEntity) entity);
            if (trinkets.isActive(Itms.COFFEE_BEAN)) {
                if (effect.equals(Effects.NAUSEA) || effect.equals(Effects.MINING_FATIGUE) || effect.equals(Effects.SLOWNESS)) {
                    denyResult.run();
                }
            }
        }
    }
}
