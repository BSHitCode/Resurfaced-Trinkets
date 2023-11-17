package owmii.losttrinkets.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.item.trinkets.*;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        FireMindTrinket.onLivingUpdate(event.getEntityLiving());
        TargetHandler.onLivingUpdate(event.getEntityLiving());
        DataManager.update(event.getEntityLiving());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void saveHealthHurt(LivingHurtEvent event) {
        RubyHeartTrinket.saveHealthHurt(event.getEntityLiving());
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        event.setAmount(CommonEventHandler.onHurt(event.getSource(), event.getEntityLiving(), event.getAmount()));
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
        event.setNewSpeed(MinersPickTrinket.onBreakSpeed(event.getPlayer(), event.getOriginalSpeed()));
    }

    @SubscribeEvent
    public static void onEnderTeleport(EntityTeleportEvent.EnderEntity event) {
        StickyMindTrinket.onEnderTeleport(event.getEntityLiving(), (cancel) -> event.setCanceled(cancel));
    }

    @SubscribeEvent
    public static void setTarget(LivingSetAttackTargetEvent event) {
        TargetHandler.setTarget(event.getEntityLiving(), event.getTarget());
    }

    @SubscribeEvent
    public static void trackPlayer(PlayerEvent.StartTracking event) {
        DataManager.trackPlayer(event.getTarget(), event.getPlayer());
    }
}
