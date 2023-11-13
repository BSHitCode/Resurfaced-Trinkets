package owmii.losttrinkets.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import owmii.losttrinkets.LostTrinkets;

import static owmii.losttrinkets.LostTrinkets.LOGGER;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configs {
    public static final Marker MARKER = new MarkerManager.Log4jMarker("Config");
    public static final GeneralConfig GENERAL;
    private static final ForgeConfigSpec GENERAL_SPEC;

    private static String createConfigDir(String path) {
        try {
            Path configDir = Paths.get(FMLPaths.CONFIGDIR.get()
                    .toAbsolutePath().toString(), path);
            Files.createDirectories(configDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static void register() {
        final String path = createConfigDir(LostTrinkets.MOD_ID);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GENERAL_SPEC, LostTrinkets.MOD_ID + "/general_common.toml");
    }

    public static void refresh(ModConfig.ModConfigEvent event) {
        String type = event instanceof ModConfig.Loading ? "Loading" : "Reloading";
        ModConfig modConfig = event.getConfig();
        LOGGER.info(MARKER, type + " " + modConfig.getFileName());
        ForgeConfigSpec spec = modConfig.getSpec();
        if (spec == GENERAL_SPEC) {
            GENERAL.refresh();
        }
    }

    static {
        final Pair<GeneralConfig, ForgeConfigSpec> generalPair = new ForgeConfigSpec.Builder().configure(GeneralConfig::new);
        GENERAL = generalPair.getLeft();
        GENERAL_SPEC = generalPair.getRight();
    }
}
