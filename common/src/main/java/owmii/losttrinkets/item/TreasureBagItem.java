package owmii.losttrinkets.item;

import java.util.List;

import dev.architectury.hooks.item.ItemStackHooks;
import dev.architectury.utils.GameInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import owmii.losttrinkets.LostTrinkets;

public class TreasureBagItem extends Item {
    static final Identifier lootTableId = new Identifier(LostTrinkets.MOD_ID, "treasure_bag");

    public TreasureBagItem(Settings properties) {
        super(properties);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world instanceof ServerWorld) {
            LootContext.Builder builder = new LootContext.Builder((ServerWorld) world);
            builder.parameter(LootContextParameters.ORIGIN, player.getPos()).random(world.random.nextLong());
            builder.luck(player.getLuck()).parameter(LootContextParameters.THIS_ENTITY, player);
            LootTable lootTable = GameInstance.getServer().getLootManager().getTable(lootTableId);
            List<ItemStack> stacks = lootTable.generateLoot(builder.build(LootContextTypes.GIFT));
            stacks.forEach(stack -> ItemStackHooks.giveItem((ServerPlayerEntity) player, stack.copy()));
            if (!player.isCreative()) {
                player.getStackInHand(hand).decrement(1);
            }
        }
        return TypedActionResult.consume(player.getStackInHand(hand));
    }
}
