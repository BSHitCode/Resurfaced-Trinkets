package owmii.losttrinkets.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {
    @Inject(method = "shouldAttackPlayer", at = @At("HEAD"), cancellable = true)
    private void shouldAttackPlayer(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (trinkets.isActive(Itms.BLANK_EYES)) {
            cir.setReturnValue(false);
        }
    }
}
