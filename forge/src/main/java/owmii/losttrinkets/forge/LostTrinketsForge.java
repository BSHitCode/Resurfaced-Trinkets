package owmii.losttrinkets.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.ConfigGuiHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.client.Sounds;
import owmii.losttrinkets.config.SunkenTrinketsConfig;
import owmii.losttrinkets.entity.DarkVexEntity;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.handler.DataManager;
import owmii.losttrinkets.impl.LostTrinketsAPIImpl;
import owmii.losttrinkets.item.Itms;

@Mod(LostTrinkets.MOD_ID)
public class LostTrinketsForge {
    @CapabilityInject(PlayerData.class)
    @SuppressWarnings("ConstantConditions")
    public static Capability<PlayerData> PLAYERDATA_CAP = null;

    public LostTrinketsForge() {
        EventBuses.registerModEventBus(LostTrinkets.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        Itms.setup();
        Entities.setup();
        Sounds.setup();

        LostTrinkets.init();

        LostTrinketsAPI.init(new LostTrinketsAPIImpl());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(LostTrinketsForge::setup);
        modEventBus.addListener(LostTrinketsForge::addEntityAttributes);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                LostTrinketsForge.registerModsPage();
            }
        });

        if (FMLEnvironment.dist.isClient()) {
            try {
                Class<?> clientClass = Class.forName("owmii.losttrinkets.client.LostTrinketsClient");
                clientClass.getMethod("init").invoke(null);
            } catch (Exception exception) {
                throw new RuntimeException("Failed Lost Trinkets client-side setup", exception);
            }
        }
    }

    public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(
            ExtensionPoint.CONFIGGUIFACTORY,
            () ->
                (client, parent) -> {
                    return SunkenTrinketsConfig.builder().setParentScreen(parent).build();
                }
        );
    }

    public static void setup(FMLCommonSetupEvent event) {
        DataManager.register();
    }

    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(Entities.DARK_VEX.get(), DarkVexEntity.getAttribute().create());
    }
}