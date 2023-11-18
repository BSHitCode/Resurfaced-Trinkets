package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class LunchBagTrinket extends Trinket<LunchBagTrinket> {
    public LunchBagTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onUseFinish(LivingEntity entity, ItemStack item) {
        World world = entity.getEntityWorld();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (item.isFood()) {
                FoodComponent food = item.getItem().getFoodComponent();
                if (food != null && food.getStatusEffects().isEmpty()) {
                    if (trinkets.isActive(Itms.LUNCH_BAG) && world.random.nextInt(10) == 0) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, world.random.nextInt(200) + 100, 1, false, false));
                    }
                }
            }
        }
    }
}
