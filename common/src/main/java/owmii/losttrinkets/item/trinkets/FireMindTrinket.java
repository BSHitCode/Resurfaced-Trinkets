package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.handler.TargetHandler;
import owmii.losttrinkets.item.Itms;

public class FireMindTrinket extends Trinket<FireMindTrinket> {
    public FireMindTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onLivingUpdate(LivingEntity entity) {
        if (entity instanceof MobEntity) {
            MobEntity mob = (MobEntity) entity;
            LivingEntity target = mob.getTarget();
            if (target == null) {
                target = TargetHandler.getBrainMemorySafe(mob.getBrain(), MemoryModuleType.ATTACK_TARGET).orElse(null);
            }
            if (target instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) target;
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                if (trinkets.isActive(Itms.FIRE_MIND) && !mob.isFireImmune()) {
                    mob.setOnFireFor(3);
                }
            }
        }
    }
}
