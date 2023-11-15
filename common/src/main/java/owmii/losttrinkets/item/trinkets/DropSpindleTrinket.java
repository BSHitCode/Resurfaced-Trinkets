package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class DropSpindleTrinket extends Trinket<DropSpindleTrinket> {
    public DropSpindleTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(DamageSource source) {
        Entity entity = source.getImmediateSource();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.DROP_SPINDLE)) {
                player.inventory.armorInventory.forEach(stack -> {
                    if (player.world.rand.nextInt(2) == 0) {
                        if (!stack.isEmpty() && stack.isDamaged()) {
                            stack.setDamage(stack.getDamage() - 1);
                        }
                    }
                });
            }
        }
    }
}
