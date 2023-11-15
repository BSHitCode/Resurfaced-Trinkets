package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class MirrorShardTrinket extends Trinket<MirrorShardTrinket> {
    private static final ThreadLocal<Boolean> dealingMirrorDamage = ThreadLocal.withInitial(() -> false);

    public MirrorShardTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(LivingEntity entity, DamageSource source, float amount) {
        if (dealingMirrorDamage.get()) return;
        try {
            dealingMirrorDamage.set(true);
            mirrorDamage(entity, source, amount);
        } finally {
            dealingMirrorDamage.set(false);
        }
    }

    private static void mirrorDamage(LivingEntity entity, DamageSource source, float amount) {
        Entity trueSource = source.getTrueSource();
        Entity immediateSource = source.getImmediateSource();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trueSource instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) trueSource;
                if (trinkets.isActive(Itms.MIRROR_SHARD)) {
                    living.attackEntityFrom(DamageSource.causePlayerDamage(player), amount / 2.0F);
                }
            }
        }
    }
}
