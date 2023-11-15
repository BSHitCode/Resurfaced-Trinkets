package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class WitherNailTrinket extends Trinket<WitherNailTrinket> {
    public WitherNailTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(DamageSource source, LivingEntity entityLiving) {
        Entity entity = source.getImmediateSource();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.WITHER_NAIL)) {
                entityLiving.addPotionEffect(new EffectInstance(Effects.WITHER, 120, 0));
            }
        }
    }
}
