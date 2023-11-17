package owmii.losttrinkets.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import owmii.losttrinkets.handler.UnlockHandler;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Inject(
        method = "onItemUse",
        at = @At(
            value = "INVOKE",
            target = 
                "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FF)V",
            shift = At.Shift.BEFORE
        )
    )
    private void onHoeUse(ItemUseContext context, CallbackInfoReturnable<ActionResultType> ci) {
        UnlockHandler.useHoe(context.getPlayer());
    }

}
