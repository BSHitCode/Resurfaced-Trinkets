package owmii.losttrinkets.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import owmii.losttrinkets.handler.UnlockHandler;

@Mixin(BoneMealItem.class)
abstract class BoneMealItemMixin {

    @Inject(
        method = "useOnBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V",
            shift = At.Shift.BEFORE
        )
    )
    private void onBonemealUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ci) {
        UnlockHandler.bonemeal(context.getPlayer());
    }

}
