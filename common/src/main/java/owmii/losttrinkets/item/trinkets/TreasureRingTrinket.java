package owmii.losttrinkets.item.trinkets;

import me.shedaniel.architectury.utils.GameInstance;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.item.Itms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreasureRingTrinket extends Trinket<TreasureRingTrinket> {
    public static final List<Identifier> LOOTS = new ArrayList<>();

    public TreasureRingTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    static {
        LOOTS.add(LootTables.NETHER_BRIDGE_CHEST);
        LOOTS.add(LootTables.JUNGLE_TEMPLE_CHEST);
        LOOTS.add(LootTables.BURIED_TREASURE_CHEST);
        LOOTS.add(LootTables.END_CITY_TREASURE_CHEST);
        LOOTS.add(LootTables.ABANDONED_MINESHAFT_CHEST);
        LOOTS.add(LootTables.DESERT_PYRAMID_CHEST);
        LOOTS.add(LootTables.SIMPLE_DUNGEON_CHEST);
        LOOTS.add(LootTables.STRONGHOLD_LIBRARY_CHEST);
        LOOTS.add(LootTables.STRONGHOLD_CORRIDOR_CHEST);
        LOOTS.add(LootTables.STRONGHOLD_CROSSING_CHEST);
        LOOTS.add(LootTables.VILLAGE_WEAPONSMITH_CHEST);
    }

    public static void onDrops(DamageSource source, LivingEntity target, Collection<ItemEntity> drops) {
        if (source.getAttacker() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getAttacker();
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.TREASURE_RING)) {
                if (!Entities.isNonBossEntity(target) && player.world instanceof ServerWorld) {
                    LootContext.Builder builder = new LootContext.Builder((ServerWorld) player.world);
                    builder.parameter(LootContextParameters.ORIGIN, target.getPos()).random(player.world.random.nextLong());
                    builder.luck(player.getLuck()).parameter(LootContextParameters.THIS_ENTITY, player);
                    LootTable lootTable = GameInstance.getServer().getLootManager().getTable(LOOTS.get(player.world.random.nextInt(LOOTS.size())));
                    List<ItemStack> stacks = lootTable.generateLoot(builder.build(LootContextTypes.CHEST));
                    stacks.forEach(stack -> drops.add(new ItemEntity(target.world, target.getX(), target.getY(), target.getZ(), stack)));
                }
            }
        }
    }
}
