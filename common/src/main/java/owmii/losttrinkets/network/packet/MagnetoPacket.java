package owmii.losttrinkets.network.packet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import owmii.losttrinkets.api.LostTrinketsAPI;
// import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.network.IPacket;
import owmii.losttrinkets.lib.util.Magnet;

import java.util.List;
import java.util.function.Supplier;

public class MagnetoPacket implements IPacket {
    public MagnetoPacket() {}

    public MagnetoPacket(PacketBuffer buffer) {}

    @Override
    public void encode(PacketBuffer buffer) {}

    @Override
    public void handle(PlayerEntity player) {
        // TODO: re-enable this check
        if (player != null /* && LostTrinketsAPI.getTrinkets(player).isActive(Itms.MAGNETO) */) {
            AxisAlignedBB bb = new AxisAlignedBB(player.getPosition()).grow(10);
            List<ItemEntity> entities = player.world.getEntitiesWithinAABB(ItemEntity.class, bb);
            List<ExperienceOrbEntity> orbEntities = player.world.getEntitiesWithinAABB(ExperienceOrbEntity.class, bb);
            entities.stream().filter(Magnet::canCollectManual).forEach(entity -> {
                entity.setNoPickupDelay();
                entity.onCollideWithPlayer(player);
            });
            orbEntities.stream().filter(Magnet::canCollectManual).forEach(orb -> {
                orb.delayBeforeCanPickup = 0;
                player.xpCooldown = 0;
                orb.onCollideWithPlayer(player);
            });
        }
    }
}
