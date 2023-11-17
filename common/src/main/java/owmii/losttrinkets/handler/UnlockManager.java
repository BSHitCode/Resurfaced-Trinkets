package owmii.losttrinkets.handler;

import com.google.common.collect.Sets;

import me.shedaniel.architectury.utils.GameInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
// import net.minecraftforge.fml.server.ServerLifecycleHooks;
// import net.minecraftforge.registries.ForgeRegistries;
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
                    WeightedTrinket item = WeightedRandom.getRandomItem(player.world.rand, WEIGHTED_TRINKETS);
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
                    Network.toClient(new TrinketUnlockedPacket(Objects.requireNonNull(Registry.ITEM.getKey(trinket.asItem())).toString()), player);
                    ItemStack stack = new ItemStack(trinket);
                    ITextComponent trinketName = stack.getDisplayName().deepCopy().modifyStyle(style -> {
                        return style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemHover(stack)));
                    });
                    GameInstance.getServer().getPlayerList().func_232641_a_(new TranslationTextComponent("chat.losttrinkets.unlocked.trinket", player.getDisplayName(), trinketName).mergeStyle(TextFormatting.DARK_AQUA), ChatType.SYSTEM, Util.DUMMY_UUID);
                }
                return true;
            }
        }
        return false;
    }


    static class WeightedTrinket extends WeightedRandom.Item {
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
        Set<ResourceLocation> banned = LostTrinkets.config().blackList.stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toCollection(Sets::newLinkedHashSet));
        Set<ResourceLocation> nonRandom = LostTrinkets.config().nonRandom.stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toCollection(Sets::newLinkedHashSet));
        Set<ResourceLocation> seen = Sets.newLinkedHashSet();
        LOGGER.info(MARKER, "Gathering Trinkets...");
        ALL_TRINKETS.forEach(trinket -> {
            ResourceLocation rl = Registry.ITEM.getKey(trinket.asItem());
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

        // TODO: handle server reload somehow
        // MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        // if (server != null) {
        //     server.runAsync(() -> GameInstance.getServer().getPlayerList().getPlayers()
        //             .forEach(player -> LostTrinketsAPI.getTrinkets(player).removeDisabled(player)));
        // }
    }

    public static Set<ITrinket> getTrinkets() {
        return Collections.unmodifiableSet(TRINKETS);
    }

    public static Set<ITrinket> getRandomTrinkets() {
        return Collections.unmodifiableSet(RANDOM_TRINKETS);
    }
}
