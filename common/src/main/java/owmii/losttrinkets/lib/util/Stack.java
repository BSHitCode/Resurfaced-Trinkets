package owmii.losttrinkets.lib.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class Stack {
    public static void drop(Entity entity, ItemStack stack) {
        drop(entity.world, entity.getPositionVec().add(0.0D, 0.3D, 0.0D), stack);
    }

    public static void drop(World world, Vector3d pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        entity.setPickupDelay(8);
        world.addEntity(entity);
    }
}
