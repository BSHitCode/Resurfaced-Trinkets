package owmii.losttrinkets.client.render.entity;

import me.shedaniel.architectury.registry.entity.EntityRenderers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import owmii.losttrinkets.entity.Entities;

@Environment(EnvType.CLIENT)
public class EntityRenderer {
    @Environment(EnvType.CLIENT)
    public static void register() {
        EntityRenderers.register(Entities.DARK_VEX.get(), DarkVexRenderer::new);
    }
}
