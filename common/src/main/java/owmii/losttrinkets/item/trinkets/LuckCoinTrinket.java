package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class LuckCoinTrinket extends Trinket<LuckCoinTrinket> implements ITickableTrinket {
    public LuckCoinTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient && player.age % 90 == 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 300, 1, false, false));
        }
    }
}
