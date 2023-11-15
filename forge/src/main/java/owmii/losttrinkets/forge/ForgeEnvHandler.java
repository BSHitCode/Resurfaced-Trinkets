package owmii.losttrinkets.forge;

import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import owmii.losttrinkets.EnvHandler;
import owmii.losttrinkets.lib.compat.botania.BotaniaCompat;

public class ForgeEnvHandler implements EnvHandler {
    public static final String PREVENT_REMOTE_MOVEMENT = "PreventRemoteMovement";
    public static final String ALLOW_MACHINE_REMOTE_MOVEMENT = "AllowMachineRemoteMovement";

    @Override
    public MinecraftServer getServerInstance() {
        return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
    }

    /**
     * Checks if the entity can be collected by a magnet, vacuum or similar.
     *
     * @param entity    The Entity in question
     * @param automated true if the magnet does not require a player to operate
     * @see <a href="https://github.com/comp500/Demagnetize#for-mod-developers">Demagnetize: For mod developers</a>
     */
    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean magnetCanCollect(Entity entity, boolean automated) {
        // Demagnetize standard
        CompoundNBT persistentData = entity.getPersistentData();
        if (persistentData.contains(PREVENT_REMOTE_MOVEMENT)) {
            if (!(automated && persistentData.contains(ALLOW_MACHINE_REMOTE_MOVEMENT))) {
                return false;
            }
        }

        if (BotaniaCompat.preventCollect(entity)) {
            return false;
        }

        return true;
    }
}
