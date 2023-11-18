package owmii.losttrinkets.fabric.mixin;

import java.util.ArrayList;
import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import owmii.losttrinkets.handler.CommonEventHandler;
import owmii.losttrinkets.handler.DataManager;
import owmii.losttrinkets.handler.TargetHandler;
import owmii.losttrinkets.item.trinkets.ButchersCleaverTrinket;
import owmii.losttrinkets.item.trinkets.CoffeeBeanTrinket;
import owmii.losttrinkets.item.trinkets.FireMindTrinket;
import owmii.losttrinkets.item.trinkets.GoldenMelonTrinket;
import owmii.losttrinkets.item.trinkets.GoldenSkullTrinket;
import owmii.losttrinkets.item.trinkets.LunchBagTrinket;
import owmii.losttrinkets.item.trinkets.MagicalHerbsTrinket;
import owmii.losttrinkets.item.trinkets.OxalisTrinket;
import owmii.losttrinkets.item.trinkets.TeaLeafTrinket;
import owmii.losttrinkets.item.trinkets.TreasureRingTrinket;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        FireMindTrinket.onLivingUpdate(entity);
        TargetHandler.onLivingUpdate(entity);
        DataManager.update(entity);
    }

    @Inject(method = "drop", at = @At("TAIL"))
    public void drop(DamageSource damageSourceIn, CallbackInfo ci) {
        Collection<ItemEntity> drops = new ArrayList<>();
        LivingEntity entity = (LivingEntity) (Object) this;
        ButchersCleaverTrinket.dropExtra(damageSourceIn, entity, drops);
        TreasureRingTrinket.onDrops(damageSourceIn, entity, drops);
        GoldenSkullTrinket.onDrops(damageSourceIn, entity, drops);
        drops.forEach(e -> entity.world.spawnEntity(e));
    }

    @Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
    public void canHaveStatusEffect(StatusEffectInstance potionEffect, CallbackInfoReturnable<Boolean> cir) {
        Runnable denyResult = () -> {
            cir.setReturnValue(false);
            cir.cancel();
        };
        LivingEntity entity = (LivingEntity) (Object) this;
        CoffeeBeanTrinket.onPotion(entity, potionEffect.getEffectType(), denyResult);
        MagicalHerbsTrinket.onPotion(entity, potionEffect.getEffectType(), denyResult);
        OxalisTrinket.onPotion(entity, potionEffect.getEffectType(), denyResult);
        TeaLeafTrinket.onPotion(entity, potionEffect.getEffectType(), denyResult);
    }

    @Inject(
        method = "consumeItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;spawnConsumptionEffects(Lnet/minecraft/item/ItemStack;I)V",
            shift = At.Shift.AFTER
        )
    )
    public void consumeItem(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        ItemStack item = entity.getActiveItem();
        GoldenMelonTrinket.onUseFinish(entity, item);
        LunchBagTrinket.onUseFinish(entity, item);
    }

    @Redirect(
        method = "applyDamage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"
        )
    )
    private float applyDamage(LivingEntity self, DamageSource source, float damageAmount) {
        damageAmount = CommonEventHandler.onHurt(source, self, damageAmount);
        return ((ArmorCalculationInvokerMixin)self).invokeApplyArmorToDamage(source, damageAmount);
    }

}
