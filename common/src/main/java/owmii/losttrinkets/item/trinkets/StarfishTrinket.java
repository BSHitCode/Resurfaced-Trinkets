package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class StarfishTrinket extends Trinket<StarfishTrinket> {
    public StarfishTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(DamageSource source, float amount) {
        Entity entity = source.getImmediateSource();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.STARFISH)) {
                player.giveExperiencePoints((int) (amount / 2.0F));
            }
        }
    }
}
