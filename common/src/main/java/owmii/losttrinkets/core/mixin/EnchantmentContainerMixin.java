package owmii.losttrinkets.core.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.util.IWorldPosCallable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import javax.annotation.Nullable;

@Mixin(EnchantmentContainer.class)
public class EnchantmentContainerMixin {
    @Nullable
    private PlayerEntity player;

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/util/IWorldPosCallable;)V", at = @At("RETURN"))
    private void enchantmentContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable, CallbackInfo ci) {
        this.player = playerInventory.player;
    }

    @ModifyArg(
        method = "func_217002_a", // func_217002_a is a lambda in onCraftMatrixChanged
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/enchantment/EnchantmentHelper;calcItemStackEnchantability(Ljava/util/Random;IILnet/minecraft/item/ItemStack;)I"
        ),
        index = 2,
        remap = false
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
