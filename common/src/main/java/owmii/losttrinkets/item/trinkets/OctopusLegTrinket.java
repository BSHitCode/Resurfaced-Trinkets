package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class OctopusLegTrinket extends Trinket<OctopusLegTrinket> {
    public OctopusLegTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onAttack(LivingEntity entity, DamageSource source) {
        World world = entity.getEntityWorld();
        if (!(world instanceof ServerWorld)) return;
        if (source == null) return;
        Entity immediateSource = source.getImmediateSource();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (immediateSource instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) immediateSource;
                if (trinkets.isActive(Itms.OCTOPUS_LEG)) {
                    MinecraftServer server = ((ServerWorld) world).getServer();
                    // Delay disarming till after goal ticking to avoid crashing
                    server.enqueue(new TickDelayedTask(server.getTickCounter(), () -> {
                        disarm(world, living);
                    }));
                }
            }
        }
    }

    private static void disarm(World world, LivingEntity living) {
        if (!living.isAlive()) return;
        ItemStack stack = living.getHeldItemMainhand();
        if (!stack.isEmpty() && world.rand.nextInt(5) == 0) {
            ItemStack stack1 = stack.copy();
            if (stack1.isDamageable()) {
                if (!stack1.isDamaged()) {
                    int damage = stack1.getMaxDamage();
                    if (damage > 10) {
                        damage /= 2;
                        damage = 10 + world.rand.nextInt(damage);
                    }
                    stack1.setDamage(damage);
                }
            }
            living.entityDropItem(stack1);
            living.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
    }
}
