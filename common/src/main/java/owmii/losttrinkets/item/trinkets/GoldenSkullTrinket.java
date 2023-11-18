package owmii.losttrinkets.item.trinkets;

import java.util.Collection;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class GoldenSkullTrinket extends Trinket<GoldenSkullTrinket> {
    public GoldenSkullTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onDrops(DamageSource source, LivingEntity target, Collection<ItemEntity> drops) {
        if (source.getAttacker() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getAttacker();
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.GOLDEN_SKULL)) {
                if (target instanceof HostileEntity) {
                    if (target.world.random.nextInt(20) == 0) {
                        drops.add(new ItemEntity(target.world, target.getX(), target.getY(), target.getZ(), new ItemStack(Itms.TREASURE_BAG.get())));
                    }
                }
            }
        }
    }
}
