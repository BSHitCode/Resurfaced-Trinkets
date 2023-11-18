package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

import java.util.List;
import java.util.function.Consumer;

public class StickyMindTrinket extends Trinket<StickyMindTrinket> {
    public StickyMindTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onEnderTeleport(LivingEntity entity, Consumer<Boolean> setCanceled) {
        Box bb = new Box(entity.getBlockPos()).expand(16);
        List<PlayerEntity> players = entity.world.getNonSpectatingEntities(PlayerEntity.class, bb);
        for (PlayerEntity player : players) {
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.STICKY_MIND)) {
                setCanceled.accept(true);
                break;
            }
        }
    }
}
