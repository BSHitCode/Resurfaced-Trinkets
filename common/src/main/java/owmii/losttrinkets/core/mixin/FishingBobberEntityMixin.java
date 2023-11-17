package owmii.losttrinkets.core.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

@Mixin(FishingBobberEntity.class)
abstract class FishingBobberEntityMixin {
    @Redirect(
        method = "handleHookRetraction",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/loot/LootTable;generate(Lnet/minecraft/loot/LootContext;)Ljava/util/List;"
        )
    )
    private List<ItemStack> handleHookRetraction(LootTable table, LootContext context) {
        FishingBobberEntity hook = (FishingBobberEntity) (Object) this;
        PlayerEntity player = hook.func_234606_i_();
        if (player instanceof ServerPlayerEntity) {
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.TREBLE_HOOKS)) {
                System.out.println("3 times the charm!");
                List<ItemStack> result = table.generate(context);
                result.addAll(table.generate(context));
                result.addAll(table.generate(context));
                return result;
            }
        }
        return table.generate(context);
    }
}
