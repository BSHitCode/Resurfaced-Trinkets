package owmii.losttrinkets.core.mixin;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
    @Inject(method = "getReputation", at = @At("TAIL"), cancellable = true)
    public void getReputation(PlayerEntity player, CallbackInfoReturnable<Integer> cir) {
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (trinkets.isActive(Itms.KARMA)) {
            cir.setReturnValue(cir.getReturnValueI() + 100);
        }
    }
}
