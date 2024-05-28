package owmii.losttrinkets.forge.handler;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.handler.*;
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
        CoffeeBeanTrinket.onPotion(event.getEntityLiving(), event.getPotionEffect().getEffectType(), denyResult);
        MagicalHerbsTrinket.onPotion(event.getEntityLiving(), event.getPotionEffect().getEffectType(), denyResult);
        OxalisTrinket.onPotion(event.getEntityLiving(), event.getPotionEffect().getEffectType(), denyResult);
        TeaLeafTrinket.onPotion(event.getEntityLiving(), event.getPotionEffect().getEffectType(), denyResult);
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        CreepoTrinket.resetExplosion(event.getPlayer(), event.getTarget());
    }

    @SubscribeEvent
    public static void onLooting(LootingLevelEvent event) {
        DamageSource source = event.getDamageSource();
        if (source == null) return;
        if (source.getAttacker() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getAttacker();
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
