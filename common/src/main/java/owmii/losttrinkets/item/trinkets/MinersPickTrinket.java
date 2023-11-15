package owmii.losttrinkets.item.trinkets;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class MinersPickTrinket extends Trinket<MinersPickTrinket> {
    public MinersPickTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onBreakSpeed(PlayerEntity player, Supplier<Float> getOriginalSpeed, Consumer<Float> setNewSpeed) {
        if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.MINERS_PICK)) {
            setNewSpeed.accept(getOriginalSpeed.get() + 3.7F);
        }
    }
}
