package owmii.losttrinkets.handler;

import com.google.common.collect.Sets;

import me.shedaniel.architectury.utils.GameInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.registry.Registry;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.network.Network;
import owmii.losttrinkets.network.packet.TrinketUnlockedPacket;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static owmii.losttrinkets.config.SunkenTrinketsConfig.MARKER;
import static owmii.losttrinkets.LostTrinkets.LOGGER;

public class UnlockManager {
    private static final Set<ITrinket> ALL_TRINKETS = Registry.ITEM.stream()
            .filter(item -> item instanceof ITrinket)
            .map(item -> (ITrinket) item)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    private static final Set<ITrinket> TRINKETS = Sets.newLinkedHashSet(ALL_TRINKETS);
    private static final Set<ITrinket> RANDOM_TRINKETS = Sets.newLinkedHashSet(ALL_TRINKETS);
    private static final List<WeightedTrinket> WEIGHTED_TRINKETS = new ArrayList<>();

    @Nullable
    public static ITrinket unlock(PlayerEntity player, boolean checkDelay) {
        if (player instanceof ServerPlayerEntity) {
            PlayerData data = LostTrinketsAPI.getData(player);
            if (!checkDelay || data.unlockDelay <= 0) {
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                WEIGHTED_TRINKETS.clear();
                WEIGHTED_TRINKETS.addAll(RANDOM_TRINKETS.stream()
                        .filter(trinket -> !trinkets.has(trinket))
                        .map(WeightedTrinket::new)
                        .collect(Collectors.toList()));
                if (!WEIGHTED_TRINKETS.isEmpty()) {
                    WeightedTrinket item = WeightedPicker.getRandom(player.world.random, WEIGHTED_TRINKETS);
                    unlock(player, item.trinket, checkDelay);
                }
            }
        }
        return null;
    }

    public static boolean unlock(PlayerEntity player, ITrinket trinket, boolean checkDelay) {
        return unlock(player, trinket, checkDelay, true);
    }

    public static boolean unlock(PlayerEntity player, ITrinket trinket, boolean checkDelay, boolean doNotification) {
        PlayerData data = LostTrinketsAPI.getData(player);
        if (!checkDelay || data.unlockDelay <= 0) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (LostTrinketsAPI.get().isEnabled(trinket) && trinkets.give(trinket)) {
                if (checkDelay) {
                    data.unlockDelay = LostTrinkets.config().unlockCooldown;
                }
                if (doNotification) {
                    Network.toClient(new TrinketUnlockedPacket(Objects.requireNonNull(Registry.ITEM.getId(trinket.asItem())).toString()), player);
                    ItemStack stack = new ItemStack(trinket);
                    Text trinketName = stack.getName().shallowCopy().styled(style -> {
                        return style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(stack)));
                    });
                    GameInstance.getServer().getPlayerManager().broadcastChatMessage(new TranslatableText("chat.losttrinkets.unlocked.trinket", player.getDisplayName(), trinketName).formatted(Formatting.DARK_AQUA), MessageType.SYSTEM, Util.NIL_UUID);
                }
                return true;
            }
        }
        return false;
    }


    static class WeightedTrinket extends WeightedPicker.Entry {
        private final ITrinket trinket;

        public WeightedTrinket(ITrinket trinket) {
            super(trinket.getRarity().getWeight());
            this.trinket = trinket;
        }

        public ITrinket getTrinket() {
            return this.trinket;
        }
    }

    public static void refresh() {
        Set<Identifier> banned = LostTrinkets.config().blackList.stream()
                .map(Identifier::new)
                .collect(Collectors.toCollection(Sets::newLinkedHashSet));
        Set<Identifier> nonRandom = LostTrinkets.config().nonRandom.stream()
                .map(Identifier::new)
                .collect(Collectors.toCollection(Sets::newLinkedHashSet));
        Set<Identifier> seen = Sets.newLinkedHashSet();
        LOGGER.info(MARKER, "Gathering Trinkets...");
        ALL_TRINKETS.forEach(trinket -> {
            Identifier rl = Registry.ITEM.getId(trinket.asItem());
            seen.add(rl);
            if (banned.contains(rl)) {
                TRINKETS.remove(trinket);
                RANDOM_TRINKETS.remove(trinket);
                LOGGER.info(MARKER, "Banned: " + rl);
            } else {
                TRINKETS.add(trinket);
                if (trinket.isUnlockable() && !nonRandom.contains(rl)) {
                    RANDOM_TRINKETS.add(trinket);
                    LOGGER.debug(MARKER, "Enabled: " + rl);
                } else {
                    RANDOM_TRINKETS.remove(trinket);
                    LOGGER.info(MARKER, "Non-Random: " + rl);
                }
            }
        });
        // Summary
        LOGGER.info(MARKER, "All: " + ALL_TRINKETS.size());
        LOGGER.info(MARKER, "Enabled: " + TRINKETS.size() + " Disabled: " + (ALL_TRINKETS.size() - TRINKETS.size()));
        LOGGER.info(MARKER, "Random: " + RANDOM_TRINKETS.size() + " Non-Random: " + (TRINKETS.size() - RANDOM_TRINKETS.size()));
        // Check configs
        banned.stream().filter(rl -> !seen.contains(rl))
                .forEach(rl -> LOGGER.warn(MARKER, "Unknown Banned Trinket: " + rl));
        nonRandom.stream().filter(rl -> !seen.contains(rl))
                .forEach(rl -> LOGGER.warn(MARKER, "Unknown Non-Random Trinket: " + rl));
        nonRandom.stream().filter(banned::contains)
                .forEach(rl -> LOGGER.warn(MARKER, "Redundant Non-Random Trinket (already banned): " + rl));
        // Remove banned trinkets from current players if server is running

        MinecraftServer server = GameInstance.getServer();
        if (server != null) {
            server.submit(() -> GameInstance.getServer().getPlayerManager().getPlayerList()
                    .forEach(player -> LostTrinketsAPI.getTrinkets(player).removeDisabled(player)));
        }
    }

    public static Set<ITrinket> getTrinkets() {
        return Collections.unmodifiableSet(TRINKETS);
    }

    public static Set<ITrinket> getRandomTrinkets() {
        return Collections.unmodifiableSet(RANDOM_TRINKETS);
    }
}
