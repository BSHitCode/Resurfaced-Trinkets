package owmii.losttrinkets.item.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class OctopusLegTrinket extends Trinket<OctopusLegTrinket> {
    public OctopusLegTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void onAttack(LivingEntity entity, DamageSource source) {
        World world = entity.getEntityWorld();
        if (!(world instanceof ServerWorld)) return;
        if (source == null) return;
        Entity immediateSource = source.getSource();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (immediateSource instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) immediateSource;
                if (trinkets.isActive(Itms.OCTOPUS_LEG)) {
                    MinecraftServer server = ((ServerWorld) world).getServer();
                    // Delay disarming till after goal ticking to avoid crashing
                    server.send(new ServerTask(server.getTicks(), () -> {
                        disarm(world, living);
                    }));
                }
            }
        }
    }

    private static void disarm(World world, LivingEntity living) {
        if (!living.isAlive()) return;
        ItemStack stack = living.getMainHandStack();
        if (!stack.isEmpty() && world.random.nextInt(5) == 0) {
            ItemStack stack1 = stack.copy();
            if (stack1.isDamageable()) {
                if (!stack1.isDamaged()) {
                    int damage = stack1.getMaxDamage();
                    if (damage > 10) {
                        damage /= 2;
                        damage = 10 + world.random.nextInt(damage);
                    }
                    stack1.setDamage(damage);
                }
            }
            living.dropStack(stack1);
            living.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
    }
}
