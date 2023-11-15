package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

import java.util.List;
import java.util.function.Consumer;

public class StickyMindTrinket extends Trinket<StickyMindTrinket> {
    public StickyMindTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onEnderTeleport(LivingEntity entity, Consumer<Boolean> setCanceled) {
        AxisAlignedBB bb = new AxisAlignedBB(entity.getPosition()).grow(16);
        List<PlayerEntity> players = entity.world.getEntitiesWithinAABB(PlayerEntity.class, bb);
        for (PlayerEntity player : players) {
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.STICKY_MIND)) {
                setCanceled.accept(true);
                break;
            }
        }
    }
}
