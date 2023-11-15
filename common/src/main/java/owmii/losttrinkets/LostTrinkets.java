package owmii.losttrinkets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.shedaniel.autoconfig.ConfigHolder;
import owmii.losttrinkets.config.SunkenTrinketsConfig;
import owmii.losttrinkets.network.Network;

public class LostTrinkets {
    public static final String MOD_ID = "losttrinkets";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final ConfigHolder<SunkenTrinketsConfig> CONFIG = SunkenTrinketsConfig.register();

    public static SunkenTrinketsConfig config() {
        return CONFIG.getConfig();
    }

    public static void init() {
        Network.register();
    }
}
