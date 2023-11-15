package owmii.losttrinkets.client;

import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import owmii.losttrinkets.client.handler.EventHandler;
import owmii.losttrinkets.client.handler.KeyHandler;
import owmii.losttrinkets.client.handler.hud.HudHandler;
import owmii.losttrinkets.client.render.entity.EntityRenderer;
import owmii.losttrinkets.client.render.tile.TileRenderer;
import owmii.losttrinkets.client.screen.Screens;
import owmii.losttrinkets.item.trinkets.MagnetoTrinket;

public final class LostTrinketsClient {
    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(LostTrinketsClient::clientSetup);

        HudHandler.register();
        MagnetoTrinket.register();

        MinecraftForge.EVENT_BUS.addListener((RenderLivingEvent.Pre event) -> {
            EventHandler.onBreakSpeed(
                event.getEntity(),
                event.getRenderer(),
                event.getMatrixStack(),
                event.getPartialRenderTick(),
                event.getBuffers(),
                (cancel) -> event.setCanceled(cancel)
            );
        });
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        EntityRenderer.register();

        event.enqueueWork(() -> {
            TileRenderer.register();
            Screens.register();
            KeyHandler.register();
            KeyHandler.TRINKET_GUI.setKeyConflictContext(KeyConflictContext.IN_GAME);
            KeyHandler.MAGNETO.setKeyConflictContext(KeyConflictContext.IN_GAME);
        });
    }
}
