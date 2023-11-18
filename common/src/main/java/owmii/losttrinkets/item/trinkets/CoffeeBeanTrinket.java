package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class CoffeeBeanTrinket extends Trinket<CoffeeBeanTrinket> {
    public CoffeeBeanTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onPotion(LivingEntity entity, StatusEffect effect, Runnable denyResult) {
        if (entity instanceof PlayerEntity) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets((PlayerEntity) entity);
            if (trinkets.isActive(Itms.COFFEE_BEAN)) {
                if (effect.equals(StatusEffects.NAUSEA) || effect.equals(StatusEffects.MINING_FATIGUE) || effect.equals(StatusEffects.SLOWNESS)) {
                    denyResult.run();
                }
            }
        }
    }
}
