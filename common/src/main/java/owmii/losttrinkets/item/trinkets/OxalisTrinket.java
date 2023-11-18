package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class OxalisTrinket extends Trinket<OxalisTrinket> {
    public OxalisTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onPotion(LivingEntity entity, StatusEffect effect, Runnable denyResult) {
        if (entity instanceof PlayerEntity) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets((PlayerEntity) entity);
            if (trinkets.isActive(Itms.OXALIS)) {
                if (effect.equals(StatusEffects.BAD_OMEN) || effect.equals(StatusEffects.UNLUCK)) {
                    denyResult.run();
                }
            }
        }
    }

    @Override
    public void onActivated(World world, BlockPos pos, PlayerEntity player) {
        if (world.isClient) return;
        player.removeStatusEffect(StatusEffects.BAD_OMEN);
        player.removeStatusEffect(StatusEffects.UNLUCK);
    }
}
