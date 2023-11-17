package owmii.losttrinkets.item;

import java.util.List;

import me.shedaniel.architectury.hooks.ItemStackHooks;
import me.shedaniel.architectury.utils.GameInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import owmii.losttrinkets.LostTrinkets;

public class TreasureBagItem extends Item {
    static final ResourceLocation lootTableId = new ResourceLocation(LostTrinkets.MOD_ID, "treasure_bag");

    public TreasureBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (world instanceof ServerWorld) {
            LootContext.Builder builder = new LootContext.Builder((ServerWorld) world);
            builder.withParameter(LootParameters.ORIGIN, player.getPositionVec()).withSeed(world.rand.nextLong());
            builder.withLuck(player.getLuck()).withParameter(LootParameters.THIS_ENTITY, player);
            LootTable lootTable = GameInstance.getServer().getLootTableManager().getLootTableFromLocation(lootTableId);
            List<ItemStack> stacks = lootTable.generate(builder.build(LootParameterSets.GIFT));
            stacks.forEach(stack -> ItemStackHooks.giveItem((ServerPlayerEntity) player, stack.copy()));
            if (!player.isCreative()) {
                player.getHeldItem(hand).shrink(1);
            }
        }
        return ActionResult.resultConsume(player.getHeldItem(hand));
    }
}
