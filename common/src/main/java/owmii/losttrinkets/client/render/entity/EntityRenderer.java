package owmii.losttrinkets.client.render.entity;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import owmii.losttrinkets.entity.Entities;

@Environment(EnvType.CLIENT)
public class EntityRenderer {
    @Environment(EnvType.CLIENT)
    public static void register() {
        EntityRendererRegistry.register(() -> Entities.DARK_VEX.get(), context -> new DarkVexRenderer(context));
    }
}
