package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.trinket.ITargetingTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.entity.ai.BigFootGoal;

public class BigFootTrinket extends Trinket<BigFootTrinket> implements ITargetingTrinket {
    public BigFootTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void addAvoidGoal(Entity entity) {
        if (entity instanceof PathAwareEntity) {
            PathAwareEntity mob = (PathAwareEntity) entity;
            mob.goalSelector.add(-1, new BigFootGoal(mob));
        }
    }

    public boolean preventTargeting(MobEntity mob, PlayerEntity player, boolean notAttacked) {
        return mob.isBaby();
    }
}
