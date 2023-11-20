package owmii.losttrinkets.fabric.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {
    private PlayerEntity player;

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("RETURN"))
    private void enchantmentContainer(int id, PlayerInventory playerInventory, ScreenHandlerContext worldPosCallable, CallbackInfo ci) {
        this.player = playerInventory.player;
    }

    @ModifyArg(
        method = "method_17411", // lambda in onContentChanged
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/enchantment/EnchantmentHelper;calculateRequiredExperienceLevel(Ljava/util/Random;IILnet/minecraft/item/ItemStack;)I"
        ),
        index = 2
        // No usage of "refmap = false" here, or it will fail in a release build!
    )
    private int modifyPower(int in) {
        if (this.player != null) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(this.player);
            if (trinkets.isActive(Itms.BOOK_O_ENCHANTING)) {
                return 15;
            }
        }
        return in;
    }
}
