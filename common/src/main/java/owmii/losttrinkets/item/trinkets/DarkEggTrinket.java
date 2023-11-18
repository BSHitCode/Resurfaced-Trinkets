package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.entity.DarkVexEntity;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.item.Itms;

public class DarkEggTrinket extends Trinket<DarkEggTrinket> {
    public DarkEggTrinket(Rarity rarity, Settings properties) {
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
                if (trinkets.isActive(Itms.DARK_EGG)) {
                    int entities = world.getNonSpectatingEntities(DarkVexEntity.class, new Box(player.getBlockPos()).expand(16)).size();
                    if (entities < 6) {
                        for (int i = 0; i < 3; i++) {
                            DarkVexEntity vex = Entities.DARK_VEX.get().create(world);
                            if (vex != null && world instanceof ServerWorldAccess) {
                                vex.initialize((ServerWorldAccess) world, world.getLocalDifficulty(player.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
                                vex.setTarget(living);
                                vex.setOwner(player);
                                vex.setBoundOrigin(player.getBlockPos());
                                vex.setPosition(player.getX(), player.getY(), player.getZ());
                                world.spawnEntity(vex);
                            }
                        }
                    }
                }
            }
        }
    }
}
