package owmii.losttrinkets.fabric.mixin;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import owmii.losttrinkets.item.trinkets.StickyMindTrinket;

@Debug(export = true)
@Mixin(ShulkerEntity.class)
abstract class ShulkerEntityMixin {
    @Inject(method = "tryTeleportToNewPosition", at = @At("HEAD"), cancellable = true)
    protected void tryTeleportToNewPosition(CallbackInfoReturnable<Boolean> cir) {
        ShulkerEntity shulker = (ShulkerEntity) (Object) this;
        if (!shulker.isAIDisabled() && shulker.isAlive()) {
            StickyMindTrinket.onEnderTeleport((LivingEntity) (Object) this, (cancel) -> {
                if (cancel) {
                    cir.setReturnValue(false);
                    cir.cancel();
                }
            });
        } else {
            // Mimic vanilla behaviour by returning "true" when the AI is disabled or the shulker is dead
            // Dont know why tho.
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
