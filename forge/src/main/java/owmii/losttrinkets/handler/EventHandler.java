package owmii.losttrinkets.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.item.trinkets.*;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (event.phase == TickEvent.Phase.END) {
            PlayerData data = LostTrinketsAPI.getData(player);
            if (data.unlockDelay > 0) {
                data.unlockDelay--;
            }
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            trinkets.getTickable().forEach(trinket -> trinket.tick(player.world, player.getPosition(), player));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void saveHealthTickStart(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            RubyHeartTrinket.saveHealthTickStart();
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        FireMindTrinket.onLivingUpdate(event.getEntityLiving());
        TargetHandler.onLivingUpdate(event.getEntityLiving());
    }

    @SubscribeEvent
    public static void onExplosionStart(ExplosionEvent.Start event) {
        Entity entity = event.getExplosion().getExploder();
        if (entity instanceof CreeperEntity) {
            CreeperEntity creeper = ((CreeperEntity) entity);
            LivingEntity target = creeper.getAttackTarget();
            if (target instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) target;
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                if (trinkets.isActive(Itms.CREEPO)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void joinWorld(EntityJoinWorldEvent event) {
        OctopickTrinket.collectDrops(event.getEntity(), (cancel) -> event.setCanceled(cancel));
        BigFootTrinket.addAvoidGoal(event.getEntity());
    }

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        if (source == null) return;
        if (BlazeHeartTrinket.isImmuneToFire(event.getEntityLiving(), event.getSource())) {
            event.setCanceled(true);
        }
        MadAuraTrinket.onAttack(event.getEntityLiving(), event.getSource(), (cancel) -> event.setCanceled(cancel));
        OctopusLegTrinket.onAttack(event.getEntityLiving(), event.getSource());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void saveHealthHurt(LivingHurtEvent event) {
        RubyHeartTrinket.saveHealthHurt(event.getEntityLiving());
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source == null) return;

        DarkDaggerTrinket.onHurt(event.getSource(), event.getAmount(), event.getEntityLiving());
        DarkEggTrinket.onHurt(event.getEntityLiving(), event.getSource());
        DropSpindleTrinket.onHurt(event.getSource());
        EmberTrinket.onHurt(event.getEntityLiving(), event.getSource());
        GoldenSwatterTrinket.onHurt(event.getEntityLiving(), event.getSource());
        MadPiggyTrinket.onHurt(event.getEntityLiving(), event.getSource());
        MirrorShardTrinket.onHurt(event.getEntityLiving(), event.getSource(), event.getAmount());
        SerpentToothTrinket.onHurt(event.getSource(), event.getEntityLiving());
        StarfishTrinket.onHurt(event.getSource(), event.getAmount());
        SlingshotTrinket.onHurt(event.getSource(), event.getEntityLiving());
        WitherNailTrinket.onHurt(event.getSource(), event.getEntityLiving());

        if (source.getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getTrueSource();
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            float amount = event.getAmount();
            if (trinkets.isActive(Itms.SILVER_NAIL)) {
                amount *= 1.1F;
            }
            if (trinkets.isActive(Itms.GLORY_SHARDS)) {
                amount *= 1.2F;
            }
            event.setAmount(amount);
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        if (source == null) return;
        RubyHeartTrinket.onDeath(event.getSource(), event.getEntityLiving(), (cancel) -> event.setCanceled(cancel));
    }

    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        ButchersCleaverTrinket.dropExtra(event.getSource(), event.getEntityLiving(), event.getDrops());
        TreasureRingTrinket.onDrops(event.getSource(), event.getEntityLiving(), event.getDrops());
        GoldenSkullTrinket.onDrops(event.getSource(), event.getEntityLiving(), event.getDrops());
    }

    @SubscribeEvent
    public static void onPotion(PotionEvent.PotionApplicableEvent event) {
        Runnable denyResult = () -> event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
        CoffeeBeanTrinket.onPotion(event.getEntityLiving(), event.getPotionEffect().getPotion(), denyResult);
        MagicalHerbsTrinket.onPotion(event.getEntityLiving(), event.getPotionEffect().getPotion(), denyResult);
        OxalisTrinket.onPotion(event.getEntityLiving(), event.getPotionEffect().getPotion(), denyResult);
        TeaLeafTrinket.onPotion(event.getEntityLiving(), event.getPotionEffect().getPotion(), denyResult);
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        CreepoTrinket.resetExplosion(event.getPlayer(), event.getTarget());
    }

    @SubscribeEvent
    public static void onLooting(LootingLevelEvent event) {
        DamageSource source = event.getDamageSource();
        if (source == null) return;
        if (source.getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getTrueSource();
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            int looting = event.getLootingLevel();
            if (trinkets.isActive(Itms.GOLDEN_HORSESHOE)) {
                looting++;
            }
            if (trinkets.isActive(Itms.GOLDEN_TOOTH)) {
                looting++;
            }
            event.setLootingLevel(looting);
        }
    }

    @SubscribeEvent
    public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
        GoldenMelonTrinket.onUseFinish(event.getEntityLiving(), event.getItem());
        LunchBagTrinket.onUseFinish(event.getEntityLiving(), event.getItem());
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        MinersPickTrinket.onBreakSpeed(event.getPlayer(), () -> event.getOriginalSpeed(), (v) -> event.setNewSpeed(v));
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        OctopickTrinket.onBreak(event.getPlayer(), event.getPos(), event.getState(), (cancel) -> event.setCanceled(cancel));
    }

    @SubscribeEvent
    public static void onFished(ItemFishedEvent event) {
        TrebleHooksTrinket.onFished(event.getPlayer(), event.getHookEntity());
    }

    @SubscribeEvent
    public static void onEnderTeleport(EnderTeleportEvent event) {
        StickyMindTrinket.onEnderTeleport(event.getEntityLiving(), (cancel) -> event.setCanceled(cancel));
    }

    @SubscribeEvent
    public static void setTarget(LivingSetAttackTargetEvent event) {
        TargetHandler.setTarget(event.getEntityLiving(), event.getTarget());
    }
}
