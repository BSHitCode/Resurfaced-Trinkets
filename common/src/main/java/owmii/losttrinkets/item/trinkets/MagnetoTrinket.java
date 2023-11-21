package owmii.losttrinkets.item.trinkets;

import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.client.handler.KeyHandler;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.network.Network;
import owmii.losttrinkets.network.packet.MagnetoPacket;

import java.util.List;

public class MagnetoTrinket extends Trinket<MagnetoTrinket> {
    public MagnetoTrinket(Rarity rarity, Settings properties) {
        super(rarity, properties);
    }

    public static void register() {
        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.register((player, hand) -> {
            if (KeyHandler.MAGNETO.isUnbound() && hand == Hand.MAIN_HAND) {
                trySendCollect(player);
            }
        });
    }

    public static void trySendCollect(PlayerEntity player) {
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (trinkets.isActive(Itms.MAGNETO)) {
            Network.toServer(new MagnetoPacket());
        }
    }

    @Override
    public void addTrinketDescription(ItemStack stack, List<Text> lines) {
        super.addTrinketDescription(stack, lines);
        String translationKey = Util.createTranslationKey("info", Registry.ITEM.getId(stack.getItem()));
        if (KeyHandler.MAGNETO.isUnbound()) {
            lines.add(new TranslatableText(translationKey + ".unbound").formatted(Formatting.GRAY));
        } else {
            lines.add(new TranslatableText(translationKey + ".bound", KeyHandler.MAGNETO.getBoundKeyLocalizedText()).formatted(Formatting.GRAY));
        }
    }
}
