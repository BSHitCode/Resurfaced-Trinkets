package owmii.losttrinkets.item.trinkets;

import java.util.Collection;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class ButchersCleaverTrinket extends Trinket<ButchersCleaverTrinket> {
    public ButchersCleaverTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void dropExtra(
        DamageSource source,
        LivingEntity target,
        Collection<ItemEntity> drops
    ) {
        if (source.getAttacker() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getAttacker();
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.BUTCHERS_CLEAVER)) {
                if (target instanceof AnimalEntity) {
                    if (target.world.random.nextInt(10) == 0) {
                        ItemStack stack = new ItemStack(Items.BONE, target.world.random.nextInt(2) + 1);
                        drops.add(new ItemEntity(target.world, target.getX(), target.getY(), target.getZ(), stack));
                    }
                }
            }
        }
    }
}
