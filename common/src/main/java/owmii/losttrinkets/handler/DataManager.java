package owmii.losttrinkets.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.core.IPlayerEntityExtended;
import owmii.losttrinkets.network.Network;
import owmii.losttrinkets.network.packet.SyncDataPacket;

public class DataManager {
    public static void clone(PlayerEntity oldPlayer, PlayerEntity newPlayer, boolean wasDeath) {
        PlayerData oldData = LostTrinketsAPI.getData(oldPlayer);
        PlayerData newData = LostTrinketsAPI.getData(newPlayer);
        newData.deserializeNBT(oldData.serializeNBT());

        Trinkets trinkets = LostTrinketsAPI.getTrinkets(newPlayer);
        trinkets.getActiveTrinkets().forEach(trinket -> {
            if (trinket instanceof Trinket) {
                ((Trinket<?>) trinket).applyAttributes(newPlayer);
            }
        });
        if (!wasDeath) {
            // fix player health
            newPlayer.setHealth(oldPlayer.getHealth());
        }
    }

    public static void update(LivingEntity entity) {
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            PlayerData data = LostTrinketsAPI.getData(player);
            if (data.isSync()) {
                Network.toTrackingAndSelf(new SyncDataPacket(player), player);
                data.setSync(false);
            }
        }
    }

    public static void loggedIn(PlayerEntity player) {
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        trinkets.initSlots(LostTrinkets.config().startSlots);
        trinkets.removeDisabled(player);
        sync(player);
    }

    public static void loggedOut(PlayerEntity player) {
        PlayerData data = LostTrinketsAPI.getData(player);
        data.wasFlying = player.getAbilities().flying;
    }

    public static void trackPlayer(Entity target, PlayerEntity player) {
        // When a player starts tracking another player entity, sync the target to them
        if (target instanceof ServerPlayerEntity) {
            Network.toClient(new SyncDataPacket((ServerPlayerEntity) target), player);
        }
    }

    static void sync(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            Network.toClient(new SyncDataPacket(player), player);
        }
    }

    public static PlayerData getPlayerData(PlayerEntity player) {
        return ((IPlayerEntityExtended)player).sunkentrinkets_getPlayerData();
    }
}
