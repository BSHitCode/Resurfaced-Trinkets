package owmii.losttrinkets.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public interface IPacket {
    void encode(PacketBuffer buffer);

    void handle(PlayerEntity player);
}
