package owmii.losttrinkets.forge.mixin;

import java.util.Collection;
import java.util.Collections;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ChunkManager;

@Mixin(ChunkManager.class)
public class ChunkManagerMixin {
	@Shadow
	@Final
	// We can abuse type erasure here and just get the type in the map as the accessor.
	// This allows us to avoid an access widener for the package-private `EntityTracker` subclass.
	private Int2ObjectMap<EntityTrackerAccessor> entities;

	public Collection<ServerPlayerEntity> forge_getTrackingPlayers(Entity entity) {
		EntityTrackerAccessor accessor = this.entities.get(entity.getEntityId());

		if (accessor != null) {
			return accessor.getTrackingPlayers();
		}

		return Collections.emptySet();
	}

}
