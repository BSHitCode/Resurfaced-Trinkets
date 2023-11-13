package owmii.losttrinkets.client;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import owmii.losttrinkets.client.handler.KeyHandler;
import owmii.losttrinkets.client.render.entity.EntityRenderer;
import owmii.losttrinkets.client.render.tile.TileRenderer;
import owmii.losttrinkets.client.screen.Screens;

public final class LostTrinketsClient {
    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(LostTrinketsClient::clientSetup);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        EntityRenderer.register();

        event.enqueueWork(() -> {
            TileRenderer.register();
            Screens.register();
            KeyHandler.register();
        });
    }
}
