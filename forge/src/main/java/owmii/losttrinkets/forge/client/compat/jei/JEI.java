package owmii.losttrinkets.forge.client.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.handler.UnlockManager;

@JeiPlugin
public class JEI implements IModPlugin {
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        UnlockManager.getTrinkets().stream()
            .map(ItemStack::new)
            .forEach(stack -> {
                Identifier key = Registry.ITEM.getId(stack.getItem());
                registration.addIngredientInfo(
                    stack, VanillaTypes.ITEM,
                    new TranslatableText(Util.createTranslationKey("info", key))
                );
            });
    }

    @Override
    public Identifier getPluginUid() {
        return new Identifier(LostTrinkets.MOD_ID, "main");
    }
}
