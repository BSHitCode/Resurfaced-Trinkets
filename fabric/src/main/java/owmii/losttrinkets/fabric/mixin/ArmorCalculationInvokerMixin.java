package owmii.losttrinkets.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;

@Mixin(LivingEntity.class)
interface ArmorCalculationInvokerMixin {
    @Invoker("applyArmorCalculations")
    float invokeApplyArmorCalculations(DamageSource source, float damageAmount);
}
