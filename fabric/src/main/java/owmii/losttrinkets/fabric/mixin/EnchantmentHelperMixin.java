package owmii.losttrinkets.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {

    @Inject(method = "getLooting", at = @At("RETURN"), cancellable = true)
    private static void getLooting(LivingEntity entityIn, CallbackInfoReturnable<Integer> cir) {
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            int looting = cir.getReturnValue();
            if (trinkets.isActive(Itms.GOLDEN_HORSESHOE)) {
                looting++;
            }
            if (trinkets.isActive(Itms.GOLDEN_TOOTH)) {
                looting++;
            }
            cir.setReturnValue(looting);
        }
    }

}
