package owmii.losttrinkets.lib.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Stack {
    public static void drop(Entity entity, ItemStack stack) {
        drop(entity.world, entity.getPos().add(0.0D, 0.3D, 0.0D), stack);
    }

    public static void drop(World world, Vec3d pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        entity.setPickupDelay(8);
        world.spawnEntity(entity);
    }
}
