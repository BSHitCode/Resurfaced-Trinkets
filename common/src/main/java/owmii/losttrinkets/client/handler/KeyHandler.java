package owmii.losttrinkets.client.handler;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.option.KeyBinding;
import owmii.losttrinkets.client.screen.TrinketsScreen;
import owmii.losttrinkets.item.trinkets.MagnetoTrinket;

import static net.minecraft.client.util.InputUtil.Type.KEYSYM;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class KeyHandler {
    public static final String TRINKET_CATEGORY = "key.categories.losttrinkets";
    public static final KeyBinding TRINKET_GUI = new KeyBinding("key.losttrinkets.trinket", KEYSYM, GLFW_KEY_R, TRINKET_CATEGORY);
    public static final KeyBinding MAGNETO = new KeyBinding("key.losttrinkets.magneto", KEYSYM, -1, TRINKET_CATEGORY);

    public static void register() {
        KeyMappingRegistry.register(TRINKET_GUI);
        KeyMappingRegistry.register(MAGNETO);
        ClientRawInputEvent.KEY_PRESSED.register((client, keyCode, scanCode, action, modifiers) -> {
            if (TRINKET_GUI.wasPressed()) {
                client.setScreen(new TrinketsScreen());
            }
            if (MAGNETO.wasPressed()) {
                MagnetoTrinket.trySendCollect(client.player);
            }
            return EventResult.pass();
        });
    }
}
