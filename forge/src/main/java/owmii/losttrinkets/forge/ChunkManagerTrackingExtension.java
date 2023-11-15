package owmii.losttrinkets.forge;

import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface ChunkManagerTrackingExtension {
    Collection<ServerPlayerEntity> forge_getTrackingPlayers(Entity entity);
}
