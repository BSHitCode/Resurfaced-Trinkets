package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class CreepoTrinket extends Trinket<CreepoTrinket> {
    public CreepoTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void resetExplosion(PlayerEntity player, Entity target) {
        if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.CREEPO)) {
            if (target instanceof CreeperEntity) {
                ((CreeperEntity) target).setFuseSpeed(-1);
                ((CreeperEntity) target).currentFuseTime = 0;
            }
        }
    }
}
