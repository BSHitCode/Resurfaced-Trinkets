package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class GoldenMelonTrinket extends Trinket<GoldenMelonTrinket> {
    public GoldenMelonTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onUseFinish(LivingEntity entity, ItemStack item) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (item.isFood()) {
                FoodComponent food = item.getItem().getFoodComponent();
                if (food != null && food.getStatusEffects().isEmpty()) {
                    if (trinkets.isActive(Itms.GOLDEN_MELON)) {
                        player.heal(food.getHunger());
                    }
                }
            }
        }
    }
}
