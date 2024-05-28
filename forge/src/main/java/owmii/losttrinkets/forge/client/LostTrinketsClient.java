package owmii.losttrinkets.forge.client;

import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import owmii.losttrinkets.client.handler.KeyHandler;
import owmii.losttrinkets.client.handler.hud.HudHandler;
import owmii.losttrinkets.client.render.entity.EntityRenderer;
import owmii.losttrinkets.item.trinkets.MagnetoTrinket;

public final class LostTrinketsClient {
    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(LostTrinketsClient::clientSetup);

        HudHandler.register();
        MagnetoTrinket.register();
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        EntityRenderer.register();

        event.enqueueWork(() -> {
            KeyHandler.register();
            KeyHandler.TRINKET_GUI.setKeyConflictContext(KeyConflictContext.IN_GAME);
            KeyHandler.MAGNETO.setKeyConflictContext(KeyConflictContext.IN_GAME);
        });
    }
}
