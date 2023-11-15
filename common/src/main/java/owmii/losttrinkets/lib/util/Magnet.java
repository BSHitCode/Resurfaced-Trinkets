package owmii.losttrinkets.lib.util;

import net.minecraft.entity.Entity;
import owmii.losttrinkets.EnvHandler;

public class Magnet {
    /**
     * Checks if the entity can be collected by a magnet, vacuum or similar.
     *
     * @param entity    The Entity in question
     * @param automated true if the magnet does not require a player to operate
     * @see <a href="https://github.com/comp500/Demagnetize#for-mod-developers">Demagnetize: For mod developers</a>
     */
    public static boolean canCollect(Entity entity, boolean automated) {
        if (!entity.isAlive()) {
            return false;
        }

        // Mod compatibility
        return EnvHandler.INSTANCE.magnetCanCollect(entity, automated);
    }

    /**
     * Checks if the entity should be collected manually.
     * e.g. Player using a magnet that collects items and experience.
     */
    public static boolean canCollectManual(Entity entity) {
        return canCollect(entity, false);
    }

    /**
     * Checks if the entity should be collected with automation.
     * e.g. An block that collect items or experience.
     */
    public static boolean canCollectAutomated(Entity entity) {
        return canCollect(entity, true);
    }
}
