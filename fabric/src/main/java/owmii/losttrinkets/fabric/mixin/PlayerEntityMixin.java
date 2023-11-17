package owmii.losttrinkets.fabric.mixin;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import owmii.losttrinkets.handler.CommonEventHandler;
import owmii.losttrinkets.item.trinkets.CreepoTrinket;
import owmii.losttrinkets.item.trinkets.MinersPickTrinket;
import owmii.losttrinkets.item.trinkets.RubyHeartTrinket;

@Debug(export = true)
@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin {
    @Unique
    private Entity attackTargetEntity;

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"))
    public void captureAttackTargetEntity(Entity targetEntity, CallbackInfo ci) {
        this.attackTargetEntity = targetEntity;
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("RETURN"))
    public void resetAttackTargetEntity(CallbackInfo ci) {
        this.attackTargetEntity = null;
    }

    /*
     * We're only interested in altering the flag (bl3) which controls weather or not we have a
     * critical hit, AFTER all vanilla changes are done;
     * 
     * Eg. the vanilla code stores to bl3 (idx 8) *twice*; so we want to be injected after the
     * second store happens.
     */
    @ModifyVariable(
        method = "attackTargetEntityWithCurrentItem",
        at = @At(value = "STORE", ordinal = 1),
        index = 8
    )
    public boolean modifyCriticalHitFlag(boolean original) {
        CreepoTrinket.resetExplosion((PlayerEntity) (Object) this, this.attackTargetEntity);
        return original;
    }

    @Inject(method = "getDigSpeed", at = @At("RETURN"), cancellable = true)
    public void getDigSpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(MinersPickTrinket.onBreakSpeed((PlayerEntity) (Object) this, cir.getReturnValue()));
    }

    @Redirect(
        method = "damageEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;applyArmorCalculations(Lnet/minecraft/util/DamageSource;F)F"
        )
    )
    public float damageEntity(PlayerEntity self, DamageSource source, float damageAmount) {
        RubyHeartTrinket.saveHealthHurt(self);

        damageAmount = CommonEventHandler.onHurt(source, self, damageAmount);
        return ((ArmorCalculationInvokerMixin)self).invokeApplyArmorCalculations(source, damageAmount);
    }

}
