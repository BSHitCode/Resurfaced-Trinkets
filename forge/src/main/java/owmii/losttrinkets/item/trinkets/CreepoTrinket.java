package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class CreepoTrinket extends Trinket<CreepoTrinket> {
    public CreepoTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void resetExplosion(PlayerEntity player, Entity target) {
        if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.CREEPO)) {
            if (target instanceof CreeperEntity) {
                // TODO: this just "deletes" the creeper; maybe add an "poof" effect?
                ((CreeperEntity) target).setCreeperState(-1);
                ((CreeperEntity) target).timeSinceIgnited = 0;
            }
        }
    }
}
