package owmii.losttrinkets.item.trinkets;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RubyHeartTrinket extends Trinket<RubyHeartTrinket> {
    private static HashMap<UUID, Float> lastHealths = new HashMap<>();
    private static BinaryOperator<Float> MAX = (a, b) -> Math.max(a, b);

    public RubyHeartTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void saveHealthTickStart(MinecraftServer server) {
        // Save player health at the beginning of the server tick
        lastHealths = server.getPlayerManager().getPlayerList().stream()
                .collect(Collectors.toMap(Entity::getUuid, LivingEntity::getHealth, MAX, HashMap::new));
    }

    public static void saveHealthHurt(LivingEntity entity) {
        // Save player health before the player is hurt
        if (entity instanceof ServerPlayerEntity) {
            lastHealths.merge(entity.getUuid(), entity.getHealth(), MAX);
        }
    }

    public static void onDeath(DamageSource source, LivingEntity entity, Consumer<Boolean> setCanceled) {
        if (!source.isOutOfWorld()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                boolean flag = false;
                if (trinkets.isActive(Itms.RUBY_HEART)) {
                    if (lastHealths.getOrDefault(player.getUuid(), player.getHealth()) > 6.0F) {
                        player.setHealth(1.0F);
                        setCanceled.accept(true);
                        flag = true;
                    }
                }
                if (!flag && trinkets.isActive(Itms.BROKEN_TOTEM)) {
                    if (player.world.random.nextInt(4) == 0) {
                        if (player instanceof ServerPlayerEntity) {
                            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
                            serverplayerentity.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
                            Criteria.USED_TOTEM.trigger(serverplayerentity, new ItemStack(Items.TOTEM_OF_UNDYING));
                        }
                        player.setHealth(1.0F);
                        player.clearStatusEffects();
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                        player.world.sendEntityStatus(player, (byte) 35);
                        setCanceled.accept(true);
                    }
                }
            }
        }
    }
}
