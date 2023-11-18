package owmii.losttrinkets.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;
import owmii.losttrinkets.item.trinkets.StickyMindTrinket;

@Mixin(EndermanEntity.class)
abstract class EndermanEntityMixin {

    @Inject(method = "teleportTo(DDD)Z", at = @At("HEAD"), cancellable = true)
    private void teleportTo(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        StickyMindTrinket.onEnderTeleport((LivingEntity) (Object) this, (cancel) -> {
            if (cancel) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        });
    }

}
