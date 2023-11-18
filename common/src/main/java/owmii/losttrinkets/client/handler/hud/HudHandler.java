package owmii.losttrinkets.client.handler.hud;

import com.mojang.blaze3d.systems.RenderSystem;

import me.shedaniel.architectury.event.events.GuiEvent;
import me.shedaniel.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import owmii.losttrinkets.client.screen.Textures;
import owmii.losttrinkets.lib.util.Ticker;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HudHandler {
    private static final List<Toast> TOASTS = new ArrayList<>();
    private static Ticker ticker = new Ticker(60);

    @Nullable
    private static Toast toast;

    public static void register() {
        GuiEvent.RENDER_HUD.register((matrices, tickDelta) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.currentScreen == null) {
                render(matrices, mc, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
            }
        });
        GuiEvent.RENDER_POST.register((screen, matrices, mouseX, mouseY, delta) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            render(matrices, mc, screen.width, screen.height);
        });
        ClientTickEvent.CLIENT_POST.register((mc) -> {
            if (mc.world == null && !TOASTS.isEmpty()) {
                TOASTS.clear();
                toast = null;
            }
            Iterator<Toast> itr = TOASTS.iterator();
            while (itr.hasNext()) {
                Toast b = itr.next();
                if (!b.getTicker().ended()) {
                    toast = b;
                    if (ticker.ended()) {
                        b.getTicker().onward();
                    }
                    ticker.add(5);
                } else {
                    ticker.back(5);
                    if (ticker.getTicks() <= 0) {
                        toast = null;
                        itr.remove();
                    }
                }
                if (toast != null)
                    break;
            }
            if (TOASTS.isEmpty()) {
                toast = null;
            }
        });
    }

    static void render(MatrixStack matrix, MinecraftClient mc, int width, int height) {
        if (toast != null) {
            RenderSystem.pushMatrix();
            RenderSystem.translated(width / 2.0F - Textures.TOAST.getWidth() / 2.0F, 4 - 60.0F + ticker.getTicks(), 0.0F);
            Textures.TOAST.draw(matrix, 0, 0);
            RenderSystem.pushMatrix();
            RenderSystem.translated(41.0F, 5.0F, 0.0F);
            mc.textRenderer.draw(matrix, I18n.translate("gui.losttrinkets.trinket.unlocked"), 0, 5, new Color(0xFFBA6F).getRGB());
            String s = I18n.translate(toast.getTrinket().asItem().getTranslationKey());
            s = StringUtils.abbreviate(s, 20);
            mc.textRenderer.draw(matrix, s, 0, 18, 0xF0C6E5);
            RenderSystem.popMatrix();
            RenderSystem.pushMatrix();
            RenderSystem.translated(5.0F, 5.0F, 0.0F);
            RenderSystem.scaled(2.0F, 2.0F, 2.0F);
            mc.getItemRenderer().renderInGuiWithOverrides(new ItemStack(toast.getTrinket()), 0, 0);
            RenderSystem.popMatrix();
            RenderSystem.popMatrix();
        }
    }

    public static void add(Toast toast) {
        TOASTS.add(toast);
    }
}
