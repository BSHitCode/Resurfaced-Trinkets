package owmii.losttrinkets.forge.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.player.ServerPlayerEntity;

// ThreadedAnvilChunkStorage

@Mixin(targets = "net/minecraft/world/server/ChunkManager$EntityTracker")
public interface EntityTrackerAccessor {
	@Accessor
	Set<ServerPlayerEntity> getTrackingPlayers();
}
