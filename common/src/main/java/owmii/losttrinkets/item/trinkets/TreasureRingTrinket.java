package owmii.losttrinkets.item.trinkets;

import me.shedaniel.architectury.utils.GameInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.item.Itms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TreasureRingTrinket extends Trinket<TreasureRingTrinket> {
    public static final List<ResourceLocation> LOOTS = new ArrayList<>();

    public TreasureRingTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    static {
        LOOTS.add(LootTables.CHESTS_NETHER_BRIDGE);
        LOOTS.add(LootTables.CHESTS_JUNGLE_TEMPLE);
        LOOTS.add(LootTables.CHESTS_BURIED_TREASURE);
        LOOTS.add(LootTables.CHESTS_END_CITY_TREASURE);
        LOOTS.add(LootTables.CHESTS_ABANDONED_MINESHAFT);
        LOOTS.add(LootTables.CHESTS_DESERT_PYRAMID);
        LOOTS.add(LootTables.CHESTS_SIMPLE_DUNGEON);
        LOOTS.add(LootTables.CHESTS_STRONGHOLD_LIBRARY);
        LOOTS.add(LootTables.CHESTS_STRONGHOLD_CORRIDOR);
        LOOTS.add(LootTables.CHESTS_STRONGHOLD_CROSSING);
        LOOTS.add(LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH);
    }

    public static void onDrops(DamageSource source, LivingEntity target, Collection<ItemEntity> drops) {
        if (source.getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getTrueSource();
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.TREASURE_RING)) {
                if (!Entities.isNonBossEntity(target) && player.world instanceof ServerWorld) {
                    LootContext.Builder builder = new LootContext.Builder((ServerWorld) player.world);
                    builder.withParameter(LootParameters.ORIGIN, target.getPositionVec()).withSeed(player.world.rand.nextLong());
                    builder.withLuck(player.getLuck()).withParameter(LootParameters.THIS_ENTITY, player);
                    LootTable lootTable = GameInstance.getServer().getLootTableManager().getLootTableFromLocation(LOOTS.get(player.world.rand.nextInt(LOOTS.size())));
                    List<ItemStack> stacks = lootTable.generate(builder.build(LootParameterSets.CHEST));
                    stacks.forEach(stack -> drops.add(new ItemEntity(target.world, target.getPosX(), target.getPosY(), target.getPosZ(), stack)));
                }
            }
        }
    }
}
