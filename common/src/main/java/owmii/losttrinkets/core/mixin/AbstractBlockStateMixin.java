package owmii.losttrinkets.core.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.item.trinkets.DragonBreathTrinket;

import java.util.List;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    abstract BlockState asBlockState();

    @Inject(method = "getDroppedStacks", at = @At("TAIL"), cancellable = true)
    public void getDroppedStacks(LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        LootContext context = builder.parameter(LootContextParameters.BLOCK_STATE, asBlockState()).build(LootContextTypes.BLOCK);
        List<ItemStack> drops = cir.getReturnValue();
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if (entity instanceof PlayerEntity) {
            cir.setReturnValue(DragonBreathTrinket.autoSmelt(drops, (PlayerEntity) entity));
        }
    }
}
