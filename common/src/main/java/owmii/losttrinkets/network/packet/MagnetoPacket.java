package owmii.losttrinkets.network.packet;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Box;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.network.IPacket;
import owmii.losttrinkets.lib.util.Magnet;

import java.util.List;

public class MagnetoPacket implements IPacket {
    public MagnetoPacket() {}

    public MagnetoPacket(PacketByteBuf buffer) {}

    @Override
    public void encode(PacketByteBuf buffer) {}

    @Override
    public void handle(PlayerEntity player) {
        if (player != null && LostTrinketsAPI.getTrinkets(player).isActive(Itms.MAGNETO)) {
            Box bb = new Box(player.getBlockPos()).expand(10);
            List<ItemEntity> entities = player.world.getNonSpectatingEntities(ItemEntity.class, bb);
            List<ExperienceOrbEntity> orbEntities = player.world.getNonSpectatingEntities(ExperienceOrbEntity.class, bb);
            entities.stream().filter(Magnet::canCollectManual).forEach(entity -> {
                entity.resetPickupDelay();
                entity.onPlayerCollision(player);
            });
            orbEntities.stream().filter(Magnet::canCollectManual).forEach(orb -> {
                player.experiencePickUpDelay = 0;
                orb.onPlayerCollision(player);
            });
        }
    }
}
