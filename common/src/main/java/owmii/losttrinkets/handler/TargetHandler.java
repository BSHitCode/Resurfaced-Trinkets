package owmii.losttrinkets.handler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.entity.Entities;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class TargetHandler {
    public static boolean preventTargeting(LivingEntity attacker, @Nullable LivingEntity target) {
        if (attacker instanceof MobEntity) {
            MobEntity mob = (MobEntity) attacker;
            if (target instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) target;

                // Always allow targeting for boss mobs
                if (!Entities.isNonBossEntity(mob)) {
                    return false;
                }

                // Not recently attacked by the player
                boolean notAttacked = !player.equals(mob.getAttacker()) && !player.equals(mob.getDamageTracker().getBiggestAttacker());

                return LostTrinketsAPI.getTrinkets(player).getTargeting().stream()
                        .anyMatch(trinket -> trinket.preventTargeting(mob, player, notAttacked));
            }
        }
        return false;
    }

    public static <T> Optional<T> getBrainMemorySafe(Brain<?> brain, MemoryModuleType<T> type) {
        return brain.hasMemoryModule(type) ? brain.getOptionalMemory(type) : Optional.empty();
    }

    public static void setTarget(LivingEntity living, LivingEntity target) {
        if (living instanceof MobEntity && preventTargeting(living, target)) {
            MobEntity mob = (MobEntity) living;
            mob.setTarget(null);
        }
    }

    public static void onLivingUpdate(LivingEntity living) {
        if (living instanceof MobEntity) {
            MobEntity mob = (MobEntity) living;
            // Remove anger target (mainly for sounds)
            if (mob instanceof Angerable) {
                Angerable angerable = (Angerable) mob;
                UUID targetUUID = angerable.getAngryAt();
                if (targetUUID != null && preventTargeting(mob, mob.world.getPlayerByUuid(targetUUID))) {
                    // Resets anger timer and target
                    angerable.stopAnger();
                }
            }
            // Remove attack target
            if (preventTargeting(mob, mob.getTarget())) {
                mob.setTarget(null);
            }
            // Remove attack target memory from brain
            Brain<?> brain = mob.getBrain();
            getBrainMemorySafe(brain, MemoryModuleType.ATTACK_TARGET).ifPresent(target -> {
                if (preventTargeting(mob, target)) {
                    brain.forget(MemoryModuleType.ATTACK_TARGET);
                }
            });
        }
    }
}
