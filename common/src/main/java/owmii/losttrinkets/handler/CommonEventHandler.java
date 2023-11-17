package owmii.losttrinkets.handler;

import java.util.concurrent.atomic.AtomicReference;

import me.shedaniel.architectury.event.events.BlockEvent;
import me.shedaniel.architectury.event.events.EntityEvent;
import me.shedaniel.architectury.event.events.ExplosionEvent;
import me.shedaniel.architectury.event.events.PlayerEvent;
import me.shedaniel.architectury.event.events.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.item.trinkets.BigFootTrinket;
import owmii.losttrinkets.item.trinkets.BlazeHeartTrinket;
import owmii.losttrinkets.item.trinkets.DarkDaggerTrinket;
import owmii.losttrinkets.item.trinkets.DarkEggTrinket;
import owmii.losttrinkets.item.trinkets.DropSpindleTrinket;
import owmii.losttrinkets.item.trinkets.EmberTrinket;
import owmii.losttrinkets.item.trinkets.GoldenSwatterTrinket;
import owmii.losttrinkets.item.trinkets.MadAuraTrinket;
import owmii.losttrinkets.item.trinkets.MadPiggyTrinket;
import owmii.losttrinkets.item.trinkets.MirrorShardTrinket;
import owmii.losttrinkets.item.trinkets.OctopickTrinket;
import owmii.losttrinkets.item.trinkets.OctopusLegTrinket;
import owmii.losttrinkets.item.trinkets.RubyHeartTrinket;
import owmii.losttrinkets.item.trinkets.SerpentToothTrinket;
import owmii.losttrinkets.item.trinkets.SlingshotTrinket;
import owmii.losttrinkets.item.trinkets.StarfishTrinket;
import owmii.losttrinkets.item.trinkets.WitherNailTrinket;

public class CommonEventHandler {

    public static void register() {
        TickEvent.PLAYER_POST.register((player) -> {
            PlayerData data = LostTrinketsAPI.getData(player);
            if (data.unlockDelay > 0) {
                data.unlockDelay--;
            }
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            trinkets.getTickable().forEach(trinket -> trinket.tick(player.world, player.getPosition(), player));

            if (player instanceof ServerPlayerEntity) {
                UnlockHandler.tickPlayerOnServer(player);
            }
        });
        TickEvent.SERVER_PRE.register((server) -> {
            RubyHeartTrinket.saveHealthTickStart(server);
        });
        // onLivingUpdate is handled by fabric:LivingEntityMixin.tick() forge:LivingUpdateEvent
        ExplosionEvent.PRE.register((world, explosion) -> {
            Entity entity = explosion.exploder;
            if (entity instanceof CreeperEntity) {
                CreeperEntity creeper = ((CreeperEntity) entity);
                LivingEntity target = creeper.getAttackTarget();
                if (target instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) target;
                    Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                    if (trinkets.isActive(Itms.CREEPO)) {
                        return ActionResultType.FAIL;
                    }
                }
            }
            return ActionResultType.PASS;
        });
        EntityEvent.ADD.register((entity, world) -> {
            AtomicReference<ActionResultType> result = new AtomicReference<>(ActionResultType.PASS);
            OctopickTrinket.collectDrops(entity, (cancel) -> {
                result.set(cancel ? ActionResultType.FAIL : ActionResultType.PASS);
            });
            BigFootTrinket.addAvoidGoal(entity);
            return result.get();
        });
        EntityEvent.LIVING_ATTACK.register((entity, source, amount) -> {
            if (source == null) return ActionResultType.PASS;

            AtomicReference<ActionResultType> result = new AtomicReference<>(ActionResultType.PASS);
            if (BlazeHeartTrinket.isImmuneToFire(entity, source)) {
                result.set(ActionResultType.FAIL);;
            }
            MadAuraTrinket.onAttack(entity, source, (cancel) -> {
                result.set(cancel ? ActionResultType.FAIL : ActionResultType.PASS);
            });
            OctopusLegTrinket.onAttack(entity, source);
            return result.get();
        });
        // saveHealthHurt is handed by fabric:PlayerEntityMixin.damageEntity() / forge:LivingHurtEvent
        // onHurt is handled by fabric:LivingEntityMixin.damageEntity() / fabric:PlayerEntityMixin.damageEntity() / forge:LivingHurtEvent
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (source == null) return ActionResultType.PASS;
            AtomicReference<ActionResultType> result = new AtomicReference<>(ActionResultType.PASS);
            RubyHeartTrinket.onDeath(source, entity, (cancel) -> {
                result.set(cancel ? ActionResultType.FAIL : ActionResultType.PASS);
            });
            return result.get();
        });
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            UnlockHandler.kill(source, entity);
            return ActionResultType.PASS;
        });
        // onDrops is handled by fabric:LivingEntityMixin.spawnDrops() / forge:LivingDropsEvent
        // onPotion is handled by fabric:LivingEntityMixin.isPotionApplicable() / forge:PotionApplicableEvent
        // onCriticalHit is handled by fabric:PlayerEntityMixin.modifyCriticalHitFlag() / forge:CriticalHitEvent
        // onLooting is handled by fabric:EnchantmentHelperMixin.getLootingModifier() / forge:LootingLevelEvent
        // onUseFinish fabric:LivingEntityMixin.onItemUseFinish() / forge:LivingEntityUseItemEvent.Finish
        // onBreakSpeed is handled by fabric:PlayerEntityMixin.getDigSpeed() / forge:PlayerEvent.BreakSpeed
        BlockEvent.BREAK.register((world, pos, state, player, xp) -> {
            AtomicReference<ActionResultType> result = new AtomicReference<>(ActionResultType.PASS);
            OctopickTrinket.onBreak(player, pos, state, (cancel) -> {
                result.set(cancel ? ActionResultType.FAIL : ActionResultType.PASS);
            });
            return result.get();
        });
        // onEnderTeleport is handled by fabric:EndermanEntityMixin.teleportTo() / fabric:ShulkerEntityMixin.tryTeleportToNewPosition() / forge:EntityTeleportEvent.EnderEntity
        // setTarget is handled by fabric:MobEntityMixin.setAttackTarget() / forge:LivingSetAttackTargetEvent
        PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
            DataManager.clone(oldPlayer, newPlayer, !wonGame);
        });
        PlayerEvent.PLAYER_JOIN.register((player) -> DataManager.loggedIn(player));
        PlayerEvent.PLAYER_QUIT.register((player) -> DataManager.loggedOut(player));
        PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> DataManager.sync(player));
        PlayerEvent.PLAYER_RESPAWN.register((player, conqueredEnd) -> DataManager.sync(player));
        // trackPlayer is handled by fabric:EntityTrackingEvents.START_TRACKING / forge:PlayerEvent.StartTracking
    }

    public static float onHurt(DamageSource source, LivingEntity entityLiving, float amount) {
        if (source == null) {
            return amount;
        }

        DarkDaggerTrinket.onHurt(source, amount, entityLiving);
        DarkEggTrinket.onHurt(entityLiving, source);
        DropSpindleTrinket.onHurt(source);
        EmberTrinket.onHurt(entityLiving, source);
        GoldenSwatterTrinket.onHurt(entityLiving, source);
        MadPiggyTrinket.onHurt(entityLiving, source);
        MirrorShardTrinket.onHurt(entityLiving, source, amount);
        SerpentToothTrinket.onHurt(source, entityLiving);
        StarfishTrinket.onHurt(source, amount);
        SlingshotTrinket.onHurt(source, entityLiving);
        WitherNailTrinket.onHurt(source, entityLiving);

        if (source.getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getTrueSource();
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.SILVER_NAIL)) {
                amount *= 1.1F;
            }
            if (trinkets.isActive(Itms.GLORY_SHARDS)) {
                amount *= 1.2F;
            }
        }

        return amount;
    }

}
