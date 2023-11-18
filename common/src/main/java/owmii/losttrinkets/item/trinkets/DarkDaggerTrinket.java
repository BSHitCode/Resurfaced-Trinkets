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

public class DarkDaggerTrinket extends Trinket<DarkDaggerTrinket> {
    public DarkDaggerTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onHurt(DamageSource source, float amount, LivingEntity entityLiving) {
        Entity entity = source.getSource();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.DARK_DAGGER)) {
                player.heal(Math.max(Math.min(amount, entityLiving.getHealth()) / 2.0F, 1.0F));
            }
        }
    }
}
