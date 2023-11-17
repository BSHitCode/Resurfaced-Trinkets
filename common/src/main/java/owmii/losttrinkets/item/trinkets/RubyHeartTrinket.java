package owmii.losttrinkets.item.trinkets;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RubyHeartTrinket extends Trinket<RubyHeartTrinket> {
    private static HashMap<UUID, Float> lastHealths = new HashMap<>();

    public RubyHeartTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void saveHealthTickStart(MinecraftServer server) {
        // Save player health at the beginning of the server tick
        lastHealths = server.getPlayerList().getPlayers().stream()
                .collect(Collectors.toMap(Entity::getUniqueID, LivingEntity::getHealth, Math::max, HashMap::new));
    }

    public static void saveHealthHurt(LivingEntity entity) {
        // Save player health before the player is hurt
        if (entity instanceof ServerPlayerEntity) {
            lastHealths.merge(entity.getUniqueID(), entity.getHealth(), Math::max);
        }
    }

    public static void onDeath(DamageSource source, LivingEntity entity, Consumer<Boolean> setCanceled) {
        if (!source.canHarmInCreative()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                boolean flag = false;
                if (trinkets.isActive(Itms.RUBY_HEART)) {
                    if (lastHealths.getOrDefault(player.getUniqueID(), player.getHealth()) > 6.0F) {
                        player.setHealth(1.0F);
                        setCanceled.accept(true);
                        flag = true;
                    }
                }
                if (!flag && trinkets.isActive(Itms.BROKEN_TOTEM)) {
                    if (player.world.rand.nextInt(4) == 0) {
                        if (player instanceof ServerPlayerEntity) {
                            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
                            serverplayerentity.addStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                            CriteriaTriggers.USED_TOTEM.trigger(serverplayerentity, new ItemStack(Items.TOTEM_OF_UNDYING));
                        }
                        player.setHealth(1.0F);
                        player.clearActivePotions();
                        player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                        player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                        player.world.setEntityState(player, (byte) 35);
                        setCanceled.accept(true);
                    }
                }
            }
        }
    }
}
