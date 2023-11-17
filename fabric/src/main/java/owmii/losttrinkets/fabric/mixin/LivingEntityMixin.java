package owmii.losttrinkets.fabric.mixin;

import java.util.ArrayList;
import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.registry.Registry;
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

    @Inject(method = "spawnDrops", at = @At("TAIL"))
    public void spawnDrops(DamageSource damageSourceIn, CallbackInfo ci) {
        Collection<ItemEntity> drops = new ArrayList<>();
        LivingEntity entity = (LivingEntity) (Object) this;
        ButchersCleaverTrinket.dropExtra(damageSourceIn, entity, drops);
        TreasureRingTrinket.onDrops(damageSourceIn, entity, drops);
        GoldenSkullTrinket.onDrops(damageSourceIn, entity, drops);
        drops.forEach(e -> entity.world.addEntity(e));
    }

    @Inject(method = "isPotionApplicable", at = @At("HEAD"), cancellable = true)
    public void isPotionApplicable(EffectInstance potionEffect, CallbackInfoReturnable<Boolean> cir) {
        Runnable denyResult = () -> {
            cir.setReturnValue(false);
            cir.cancel();
        };
        LivingEntity entity = (LivingEntity) (Object) this;
        CoffeeBeanTrinket.onPotion(entity, potionEffect.getPotion(), denyResult);
        MagicalHerbsTrinket.onPotion(entity, potionEffect.getPotion(), denyResult);
        OxalisTrinket.onPotion(entity, potionEffect.getPotion(), denyResult);
        TeaLeafTrinket.onPotion(entity, potionEffect.getPotion(), denyResult);
    }

    @Inject(
        method = "onItemUseFinish",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;triggerItemUseEffects(Lnet/minecraft/item/ItemStack;I)V",
            shift = At.Shift.AFTER
        )
    )
    public void onItemUseFinish(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        ItemStack item = entity.getActiveItemStack();
        GoldenMelonTrinket.onUseFinish(entity, item);
        LunchBagTrinket.onUseFinish(entity, item);
    }

    @Redirect(
        method = "damageEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;applyArmorCalculations(Lnet/minecraft/util/DamageSource;F)F"
        )
    )
    private float damageEntity(LivingEntity self, DamageSource source, float damageAmount) {
        damageAmount = CommonEventHandler.onHurt(source, self, damageAmount);
        return ((ArmorCalculationInvokerMixin)self).invokeApplyArmorCalculations(source, damageAmount);
    }

}
