package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.trinket.ITargetingTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class ThaGhostTrinket extends Trinket<ThaGhostTrinket> implements ITargetingTrinket {
    public ThaGhostTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public boolean preventTargeting(MobEntity mob, PlayerEntity player, boolean notAttacked) {
        return notAttacked && player.hasStatusEffect(StatusEffects.INVISIBILITY);
    }
}
