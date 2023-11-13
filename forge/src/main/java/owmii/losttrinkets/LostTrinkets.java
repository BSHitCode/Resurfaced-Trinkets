package owmii.losttrinkets;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.lib.network.Network;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.client.Sounds;
import owmii.losttrinkets.config.Configs;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.handler.DataManager;
import owmii.losttrinkets.impl.LostTrinketsAPIImpl;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.network.Packets;

@Mod(LostTrinkets.MOD_ID)
public class LostTrinkets {
    public static final String MOD_ID = "losttrinkets";
    public static final Network NET = new Network(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public LostTrinkets() {
        Itms.setup();
        Entities.setup();
        Sounds.setup();

        LostTrinketsAPI.init(new LostTrinketsAPIImpl());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(LostTrinkets::setup);

        Configs.register();
        modEventBus.addListener(Configs::refresh);

        if (FMLEnvironment.dist.isClient()) {
            try {
                Class clientClass = Class.forName("owmii.losttrinkets.client.LostTrinketsClient");
                clientClass.getMethod("init").invoke(null);
            } catch (Exception exception) {
                throw new RuntimeException("Failed Lost Trinkets client-side setup", exception);
            }
        }
    }

    public static void setup(FMLCommonSetupEvent event) {
        DataManager.register();
        Packets.register();
        Entities.register();
    }
}