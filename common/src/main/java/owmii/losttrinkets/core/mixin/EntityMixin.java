package owmii.losttrinkets.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

@Mixin(Entity.class)
abstract class EntityMixin {

    // While this is only used in LivingRenderer, and even annotated to be Client-only,
    // we still apply this to both sides, so Mods wo rely on it get the proper value too.
    @Inject(
        method = "isInvisibleToPlayer",
        at = @At("HEAD"),
        cancellable = true
    )
    private void isInvisibleToPlayer(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.MINDS_EYE)) {
            cir.setReturnValue(false);
        }
    }

}
