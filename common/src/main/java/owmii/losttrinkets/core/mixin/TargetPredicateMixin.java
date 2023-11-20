package owmii.losttrinkets.core.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.handler.TargetHandler;

@Mixin(TargetPredicate.class)
public class TargetPredicateMixin {
    @Inject(method = "test(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;)Z", at = @At("TAIL"), cancellable = true)
    public void canTarget(LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            if (TargetHandler.preventTargeting(attacker, target)) {
                cir.setReturnValue(false);
            }
        }
    }
}
