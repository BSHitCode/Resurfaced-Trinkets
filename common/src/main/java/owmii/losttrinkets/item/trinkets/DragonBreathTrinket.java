package owmii.losttrinkets.item.trinkets;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class DragonBreathTrinket extends Trinket<DragonBreathTrinket> {
    public DragonBreathTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static List<ItemStack> autoSmelt(List<ItemStack> stacks, PlayerEntity player) {
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, player.getMainHandStack()) <= 0) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.DRAGON_BREATH)) {
                List<ItemStack> drops1 = new ArrayList<>();
                List<ItemStack> stacks1 = new ArrayList<>(stacks);
                Iterator<ItemStack> itr = stacks1.iterator();
                while (itr.hasNext()) {
                    Optional<SmeltingRecipe> recipe = player.world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(itr.next()), player.world);
                    if (recipe.isPresent()) {
                        ItemStack output = recipe.get().getOutput().copy();
                        if (!output.isEmpty()) {
                            drops1.add(output);
                            itr.remove();
                        }
                    }
                }
                drops1.addAll(stacks1);
                return drops1;
            }
        }
        return stacks;
    }
}
