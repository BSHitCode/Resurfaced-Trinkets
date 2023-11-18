package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class EmberTrinket extends Trinket<EmberTrinket> {
    public EmberTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onHurt(LivingEntity entity, DamageSource source) {
        Entity immediateSource = source.getSource();
        if (immediateSource instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) immediateSource;
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                if (trinkets.isActive(Itms.EMBER)) {
                    living.setOnFireFor(10);
                }
            }
        }
    }
}
