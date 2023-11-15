package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class OxalisTrinket extends Trinket<OxalisTrinket> {
    public OxalisTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onPotion(LivingEntity entity, Effect effect, Runnable denyResult) {
        if (entity instanceof PlayerEntity) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets((PlayerEntity) entity);
            if (trinkets.isActive(Itms.OXALIS)) {
                if (effect.equals(Effects.BAD_OMEN) || effect.equals(Effects.UNLUCK)) {
                    denyResult.run();
                }
            }
        }
    }

    @Override
    public void onActivated(World world, BlockPos pos, PlayerEntity player) {
        if (world.isRemote) return;
        player.removePotionEffect(Effects.BAD_OMEN);
        player.removePotionEffect(Effects.UNLUCK);
    }
}
