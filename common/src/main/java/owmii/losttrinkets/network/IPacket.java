package owmii.losttrinkets.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

public interface IPacket {
    void encode(PacketByteBuf buffer);

    void handle(PlayerEntity player);
}
