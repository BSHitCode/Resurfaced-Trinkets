package owmii.losttrinkets.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import owmii.losttrinkets.handler.UnlockHandler;

@Mixin(BoneMealItem.class)
abstract class BoneMealItemMixin {

    @Inject(
        method = "onItemUse",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V",
            shift = At.Shift.BEFORE
        )
    )
    private void onBonemealUse(ItemUseContext context, CallbackInfoReturnable<ActionResultType> ci) {
        UnlockHandler.bonemeal(context.getPlayer());
    }

}
