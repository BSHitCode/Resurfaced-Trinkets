package owmii.losttrinkets.core.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.network.ServerPlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

@Mixin(FishingBobberEntity.class)
abstract class FishingBobberEntityMixin {
    @Redirect(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootContext;)Ljava/util/List;"
        )
    )
    private List<ItemStack> handleHookRetraction(LootTable table, LootContext context) {
        FishingBobberEntity hook = (FishingBobberEntity) (Object) this;
        PlayerEntity player = hook.getPlayerOwner();
        if (player instanceof ServerPlayerEntity) {
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.TREBLE_HOOKS)) {
                System.out.println("3 times the charm!");
                List<ItemStack> result = table.generateLoot(context);
                result.addAll(table.generateLoot(context));
                result.addAll(table.generateLoot(context));
                return result;
            }
        }
        return table.generateLoot(context);
    }
}
