package owmii.losttrinkets.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import owmii.losttrinkets.client.handler.KeyHandler;
import owmii.losttrinkets.client.handler.hud.HudHandler;
import owmii.losttrinkets.client.render.entity.EntityRenderer;
import owmii.losttrinkets.item.trinkets.MagnetoTrinket;

public class LostTrinketsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderer.register();
        HudHandler.register();
        MagnetoTrinket.register();
        KeyHandler.register();

        // TODO: EventHandler.onRenderLiving
    }

}
