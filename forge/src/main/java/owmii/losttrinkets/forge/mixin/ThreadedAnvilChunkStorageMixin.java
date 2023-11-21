package owmii.losttrinkets.forge.mixin;

import java.util.Collection;
import java.util.Collections;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import owmii.losttrinkets.forge.ThreadedAnvilChunkStorageTrackingExtension;

@Mixin(ThreadedAnvilChunkStorage.class)
abstract class ThreadedAnvilChunkStorageMixin implements ThreadedAnvilChunkStorageTrackingExtension {
	@Shadow
	@Final
	// We can abuse type erasure here and just get the type in the map as the accessor.
	// This allows us to avoid an access widener for the package-private `EntityTracker` subclass.
	private Int2ObjectMap<EntityTrackerAccessor> entityTrackers;

	public Collection<ServerPlayerEntity> forge_getTrackingPlayers(Entity entity) {
		EntityTrackerAccessor accessor = this.entityTrackers.get(entity.getId());

		if (accessor != null) {
			return accessor.getListeners();
		}

		return Collections.emptySet();
	}

}
