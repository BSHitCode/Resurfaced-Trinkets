package owmii.losttrinkets.client.screen;

import net.minecraft.util.Identifier;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.lib.client.screen.Texture;

public class Textures {
    public static final Texture TOAST = register("toasts", 160, 42, 0, 0);
    public static final Texture TRINKET_BG_ADD = register("trinket", 26, 26, 0, 0);
    public static final Texture TRINKET_BG_LOCKED = register("trinket", 26, 26, 26, 0);
    public static final Texture TRINKET_BG = register("trinket", 26, 26, 52, 0);
    public static final Texture TRINKET_ACTIVE_BG = register("trinket", 26, 26, 78, 0);
    public static final Texture TRINKET_PREV = register("trinket", 26, 15, 0, 26);
    public static final Texture TRINKET_NEXT = register("trinket", 26, 15, 26, 26);

    static Texture register(String path, int width, int height, int u, int v) {
        return new Texture(new Identifier(LostTrinkets.MOD_ID, "textures/gui/" + path + ".png"), width, height, u, v);
    }
}
