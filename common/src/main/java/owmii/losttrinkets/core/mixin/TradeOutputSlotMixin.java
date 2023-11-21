package owmii.losttrinkets.core.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.TradeOutputSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owmii.losttrinkets.handler.UnlockHandler;

@Mixin(TradeOutputSlot.class)
public class TradeOutputSlotMixin {
    @Inject(method = "onTakeItem", at = @At("HEAD"))
    private void trade(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        UnlockHandler.trade(player);
    }
}
