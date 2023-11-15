package owmii.losttrinkets.forge.mixin;

import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface ChunkManagerTrackingExtension {
    Collection<ServerPlayerEntity> forge_getTrackingPlayers(Entity entity);
}
