package owmii.losttrinkets.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.network.Network;
import owmii.losttrinkets.network.packet.SetInactivePacket;

import javax.annotation.Nullable;


public class TrinketOptionScreen extends AbstractLTScreen {
    private final ITrinket trinket;
    @SuppressWarnings("unused")
    private ButtonWidget button;

    @Nullable
    protected final Screen prevScreen;

    protected TrinketOptionScreen(ITrinket trinket, @Nullable Screen prevScreen) {
        super(new TranslatableText(trinket.asItem().getTranslationKey()));
        this.trinket = trinket;
        this.prevScreen = prevScreen;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 60 / 2;
        int y = this.height / 3 - 20 / 2;
        if (this.mc.player != null) {
            this.button = addButton(new ButtonWidget(x, y + 70, 60, 20, new TranslatableText("Remove"), (p_214293_1_) -> {
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(this.mc.player);
                int i = trinkets.getActiveTrinkets().indexOf(this.trinket);
                if (i >= 0) {
                    Network.toServer(new SetInactivePacket(i));
                    trinkets.setInactive(this.trinket, this.mc.player);
                    setRefreshScreen(new TrinketsScreen());
                }
            }));
        }
    }

    @Override
    public void render(MatrixStack matrix, int mx, int my, float pt) {
        renderBackground(matrix);
        int x = this.width / 2 - 16 / 2;
        int y = this.height / 3 - 16 / 2;
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(x - 16.0F, y - 16.0F, 0.0F);
        matrixStack.scale(3.0F, 3.0F, 1.0F);
        RenderSystem.applyModelViewMatrix();
        this.mc.getItemRenderer().renderInGuiWithOverrides(new ItemStack(this.trinket), 0, 0);
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();

        super.render(matrix, mx, my, pt);

        String name = I18n.translate(this.trinket.asItem().getTranslationKey());
        this.textRenderer.draw(matrix, name, 8 + x - this.textRenderer.getWidth(name) / 2, y + 32, 0x999999);
    }

    @Override
    public void onClose() {
        if (this.prevScreen instanceof TrinketsScreen) {
            this.mc.setScreen(this.prevScreen);
        }
    }
}
