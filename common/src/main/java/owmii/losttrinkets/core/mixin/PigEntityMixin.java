package owmii.losttrinkets.core.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends AnimalEntity implements ItemSteerable, Saddleable {
    protected PigEntityMixin(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canBeControlledByRider() {
        Entity entity = getPrimaryPassenger();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (player.getMainHandStack().getItem() == Items.CARROT_ON_A_STICK || player.getOffHandStack().getItem() == Items.CARROT_ON_A_STICK) {
                return true;
            } else if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.PIGGY)) {
                return player.forwardSpeed > 0.0F;
            }
        }
        return false;
    }

    @Override
    public float getSaddledSpeed() {
        Entity entity = getPrimaryPassenger();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.PIGGY)) {
                return 0.45F;
            }
        }
        return (float) (getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 0.225F);
    }
}
