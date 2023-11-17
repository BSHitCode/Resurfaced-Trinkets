package owmii.losttrinkets.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import owmii.losttrinkets.handler.TargetHandler;

@Mixin(MobEntity.class)
abstract class MobEntityMixin {
    @Inject(method = "setAttackTarget", at = @At("TAIL"))
    public void setAttackTarget(LivingEntity target, CallbackInfo ci) {
        TargetHandler.setTarget((LivingEntity) (Object) this, target);
    }
}
