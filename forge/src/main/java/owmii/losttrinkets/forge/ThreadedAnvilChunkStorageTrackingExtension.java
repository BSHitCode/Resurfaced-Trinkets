package owmii.losttrinkets.forge;

import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

public interface ThreadedAnvilChunkStorageTrackingExtension {
    Collection<ServerPlayerEntity> forge_getTrackingPlayers(Entity entity);
}
