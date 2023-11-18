package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import java.util.List;

public class MadPiggyTrinket extends Trinket<MadPiggyTrinket> {
    public MadPiggyTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onHurt(LivingEntity entity, DamageSource source) {
        World world = entity.getEntityWorld();
        Entity trueSource = source.getAttacker();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trueSource instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) trueSource;
                if (trinkets.isActive(Itms.MAD_PIGGY)) {
                    Box bb = new Box(player.getBlockPos()).expand(24.0D);
                    List<ZombifiedPiglinEntity> entities = world.getNonSpectatingEntities(ZombifiedPiglinEntity.class, bb);
                    for (ZombifiedPiglinEntity zombifiedPiglin : entities) {
                        zombifiedPiglin.setAttacker(living);
                        zombifiedPiglin.setTarget(living);
                        world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, SoundCategory.HOSTILE, 1.5F, 1.0F);
                    }
                }
            }
        }
    }
}
