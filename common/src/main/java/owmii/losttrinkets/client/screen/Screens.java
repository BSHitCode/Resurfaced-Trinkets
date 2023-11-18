package owmii.losttrinkets.client.screen;

import net.minecraft.client.MinecraftClient;

public class Screens {
    public static void checkScreenRefresh() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen instanceof AbstractLTScreen) {
            ((AbstractLTScreen) mc.currentScreen).refresh();
        }
    }
}
