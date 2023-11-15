package owmii.losttrinkets.item.trinkets;

import me.shedaniel.architectury.event.events.InteractionEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.client.handler.KeyHandler;
import owmii.losttrinkets.forge.LostTrinketsForge;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.network.packet.MagnetoPacket;

import java.util.List;

public class MagnetoTrinket extends Trinket<MagnetoTrinket> {
    public MagnetoTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void register() {
        InteractionEvent.CLIENT_RIGHT_CLICK_AIR.register((player, hand) -> {
            if (KeyHandler.MAGNETO.isInvalid() && hand == Hand.MAIN_HAND) {
                trySendCollect(player);
            }
        });
    }

    public static void trySendCollect(PlayerEntity player) {
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (trinkets.isActive(Itms.MAGNETO)) {
            LostTrinketsForge.NET.toServer(new MagnetoPacket());
        }
    }

    @Override
    public void addTrinketDescription(ItemStack stack, List<ITextComponent> lines) {
        super.addTrinketDescription(stack, lines);
        String translationKey = Util.makeTranslationKey("info", Registry.ITEM.getKey(stack.getItem()));
        if (KeyHandler.MAGNETO.isInvalid()) {
            lines.add(new TranslationTextComponent(translationKey + ".unbound").mergeStyle(TextFormatting.GRAY));
        } else {
            lines.add(new TranslationTextComponent(translationKey + ".bound", KeyHandler.MAGNETO.func_238171_j_()).mergeStyle(TextFormatting.GRAY));
        }
    }
}
