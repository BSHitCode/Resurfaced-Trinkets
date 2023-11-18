package owmii.losttrinkets.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.entity.DarkVexEntity;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.handler.DataManager;

public class LostTrinketsFabric implements ModInitializer {

    public LostTrinketsFabric() {
    }

    @Override
    public void onInitialize() {
        LostTrinkets.init();
        FabricDefaultAttributeRegistry.register(Entities.DARK_VEX.get(), DarkVexEntity.getAttribute());

        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            DataManager.trackPlayer(trackedEntity, player);
        });
    }

}
