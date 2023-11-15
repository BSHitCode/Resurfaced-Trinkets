package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.item.Itms;

public class GoldenSwatterTrinket extends Trinket<GoldenSwatterTrinket> {
    public GoldenSwatterTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(LivingEntity living, DamageSource source) {
        if (living instanceof SilverfishEntity || living instanceof EndermiteEntity) {
            Entity entity = source.getImmediateSource();
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                if (trinkets.isActive(Itms.GOLDEN_SWATTER)) {
                    if (Entities.isNonBossEntity(living)) {
                        living.setHealth(0.5F);
                    }
                }
            }
        }
    }
}
