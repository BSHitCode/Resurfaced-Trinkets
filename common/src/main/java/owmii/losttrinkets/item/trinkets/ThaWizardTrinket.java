package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.trinket.ITargetingTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class ThaWizardTrinket extends Trinket<ThaWizardTrinket> implements ITargetingTrinket {
    public ThaWizardTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public boolean preventTargeting(MobEntity mob, PlayerEntity player, boolean notAttacked) {
        return mob instanceof WitchEntity;
    }
}
