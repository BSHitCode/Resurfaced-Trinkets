package owmii.losttrinkets.core.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

@Mixin(EnderPearlItem.class)
public class EnderPearlItemMixin extends Item {
    public EnderPearlItemMixin(Settings properties) {
        super(properties);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
        player.getItemCooldownManager().set(this, 20);
        if (!world.isClient) {
            EnderPearlEntity entity = new EnderPearlEntity(world, player);
            entity.setItem(stack);
            entity.setProperties(player, player.pitch, player.yaw, 0.0F, 1.5F, 1.0F);
            world.spawnEntity(entity);
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));

        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (!player.abilities.creativeMode && !trinkets.isActive(Itms.EMPTY_AMULET)) {
            stack.decrement(1);
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}
