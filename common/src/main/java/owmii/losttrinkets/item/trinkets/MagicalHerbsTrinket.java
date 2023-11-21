package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import java.util.Iterator;

public class MagicalHerbsTrinket extends Trinket<MagicalHerbsTrinket> {
    public MagicalHerbsTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onPotion(LivingEntity entity, StatusEffect effect, Runnable denyResult) {
        if (entity instanceof PlayerEntity) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets((PlayerEntity) entity);
            if (trinkets.isActive(Itms.MAGICAL_HERBS)) {
                if (effect.getCategory().equals(StatusEffectCategory.HARMFUL) ||
                        effect.equals(StatusEffects.BAD_OMEN)) {
                    denyResult.run();
                }
            }
        }
    }

    @Override
    public void onActivated(World world, BlockPos pos, PlayerEntity player) {
        if (world.isClient) return;
        Iterator<StatusEffectInstance> iterator = player.getActiveStatusEffects().values().iterator();
        while (iterator.hasNext()) {
            StatusEffectInstance effect = iterator.next();
            if (effect.getEffectType().getCategory().equals(StatusEffectCategory.HARMFUL) ||
                    effect.getEffectType().equals(StatusEffects.BAD_OMEN)) {
                player.onStatusEffectRemoved(effect);
                iterator.remove();
            }
        }
    }
}
