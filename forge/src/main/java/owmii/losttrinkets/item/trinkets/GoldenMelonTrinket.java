package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class GoldenMelonTrinket extends Trinket<GoldenMelonTrinket> {
    public GoldenMelonTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onUseFinish(LivingEntity entity, ItemStack item) {
        World world = entity.getEntityWorld();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (item.isFood()) {
                Food food = item.getItem().getFood();
                if (food != null && food.getEffects().isEmpty()) {
                    if (trinkets.isActive(Itms.GOLDEN_MELON)) {
                        player.heal(food.getHealing());
                    }
                }
            }
        }
    }
}