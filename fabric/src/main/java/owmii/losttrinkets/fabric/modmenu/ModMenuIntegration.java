package owmii.losttrinkets.fabric.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import owmii.losttrinkets.config.SunkenTrinketsConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return SunkenTrinketsConfig.builder().setParentScreen(parent).build();
        };
    }
}
