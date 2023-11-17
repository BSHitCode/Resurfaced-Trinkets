package owmii.losttrinkets.client.handler;

import java.util.function.Consumer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

public class EventHandler {
    public static void onRenderLiving(
        LivingEntity living,
        LivingRenderer renderer,
        MatrixStack matrix,
        float partialTicks,
        IRenderTypeBuffer buffers,
        Consumer<Boolean> setCanceled
    ) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.world != null) {
            if (living instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) living;
                if (player.isPotionActive(Effects.INVISIBILITY) && LostTrinketsAPI.getTrinkets(player).isActive(Itms.THA_GHOST)) {
                    setCanceled.accept(true);
                }
            }
        }
    }
}
