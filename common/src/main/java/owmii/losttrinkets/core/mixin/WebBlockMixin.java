package owmii.losttrinkets.core.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.lib.util.Stack;

@Mixin(CobwebBlock.class)
public class WebBlockMixin extends Block {
    public WebBlockMixin(Settings properties) {
        super(properties);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        boolean flag = false;
        if (entity instanceof PlayerEntity) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets((PlayerEntity) entity);
            if (trinkets.isActive(Itms.GLASS_SHARD)) {
                world.breakBlock(pos, false);
                Stack.drop(entity, new ItemStack(Items.STRING, 1 + world.random.nextInt(2)));
                flag = true;
            }
        }
        if (!flag) {
            entity.slowMovement(state, new Vec3d(0.25D, (double) 0.05F, 0.25D));
        }
    }
}
