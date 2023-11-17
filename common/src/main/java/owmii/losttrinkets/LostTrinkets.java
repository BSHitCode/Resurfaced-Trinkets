package owmii.losttrinkets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.shedaniel.autoconfig.ConfigHolder;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.client.Sounds;
import owmii.losttrinkets.command.MainCommand;
import owmii.losttrinkets.config.SunkenTrinketsConfig;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.handler.CommonEventHandler;
import owmii.losttrinkets.impl.LostTrinketsAPIImpl;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.network.Network;

public class LostTrinkets {
    public static final String MOD_ID = "losttrinkets";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final ConfigHolder<SunkenTrinketsConfig> CONFIG = SunkenTrinketsConfig.register();

    public static SunkenTrinketsConfig config() {
        return CONFIG.getConfig();
    }

    public static void init() {
        Itms.setup();
        Entities.setup();
        Sounds.setup();
        Network.register();
        MainCommand.register();
        CommonEventHandler.register();

        LostTrinketsAPI.init(new LostTrinketsAPIImpl());
    }
}
