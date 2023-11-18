package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class SlingshotTrinket extends Trinket<SlingshotTrinket> {
    public SlingshotTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onHurt(DamageSource source, LivingEntity entityLiving) {
        Entity entity = source.getSource();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.SLINGSHOT)) {
                entityLiving.takeKnockback(1.4F, (double) MathHelper.sin(player.yaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(player.yaw * ((float) Math.PI / 180F))));
            }
        }
    }
}
