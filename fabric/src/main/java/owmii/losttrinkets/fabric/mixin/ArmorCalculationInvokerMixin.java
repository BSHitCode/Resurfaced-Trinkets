package owmii.losttrinkets.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(LivingEntity.class)
interface ArmorCalculationInvokerMixin {
    @Invoker("applyArmorToDamage")
    float invokeApplyArmorToDamage(DamageSource source, float damageAmount);
}
