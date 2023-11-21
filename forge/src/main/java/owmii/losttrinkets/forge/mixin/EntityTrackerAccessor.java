package owmii.losttrinkets.forge.mixin;

import java.util.Set;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net/minecraft/server/world/ThreadedAnvilChunkStorage$EntityTracker")
public interface EntityTrackerAccessor {
	@Accessor
	Set<ServerPlayerEntity> getListeners();
}
