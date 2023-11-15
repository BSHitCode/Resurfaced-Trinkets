package owmii.losttrinkets.network.packet;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.client.Sounds;
import owmii.losttrinkets.client.handler.hud.HudHandler;
import owmii.losttrinkets.client.handler.hud.Toast;
import owmii.losttrinkets.lib.client.util.MC;
import owmii.losttrinkets.network.IPacket;

public class TrinketUnlockedPacket implements IPacket {
    private String key;

    public TrinketUnlockedPacket(String key) {
        this.key = key;
    }

    public TrinketUnlockedPacket() {
        this("");
    }

    public TrinketUnlockedPacket(PacketBuffer buffer) {
        this(buffer.readString(32767));
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeString(this.key);
    }

    @Override
    public void handle(PlayerEntity _sender) {
        MC.player().ifPresent(player -> {
            Optional<Item> optItem = Registry.ITEM.getOptional(new ResourceLocation(this.key));
            if (!optItem.isPresent()) {
                return;
            }
            Item item = optItem.get();
            if (item instanceof ITrinket) {
                HudHandler.add(new Toast((ITrinket) item));
                player.playSound(Sounds.UNLOCK.get(), 1.0F, 1.0F);
            }
        });
    }
}
