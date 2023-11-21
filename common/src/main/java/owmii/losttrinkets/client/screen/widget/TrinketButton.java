package owmii.losttrinkets.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.lib.client.screen.Texture;

public class TrinketButton extends ButtonWidget {
    protected final ITrinket trinket;
    private final Texture texture;

    public TrinketButton(int x, int y, Texture texture, ITrinket trinket, PressAction pressable, TooltipSupplier tooltip) {
        super(x, y, texture.getWidth(), texture.getHeight(), new LiteralText(""), pressable, tooltip);
        this.trinket = trinket;
        this.texture = texture;
    }


    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float pt) {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            this.texture.draw(matrix, this.x, this.y);
            int i = (this.texture.getWidth() - 16) / 2;
            int j = (this.texture.getHeight() - 16) / 2;
            MatrixStack matrixStack = RenderSystem.getModelViewStack();
            matrixStack.push();
            MinecraftClient mc = MinecraftClient.getInstance();
            matrixStack.translate(i + this.x - 2.0F, j + this.y - 2.0F, 0.0F);
            matrixStack.scale(1.25F, 1.25F, 1.0F);
            RenderSystem.applyModelViewMatrix();
            mc.getItemRenderer().renderInGuiWithOverrides(new ItemStack(this.trinket), 0, 0);
            matrixStack.pop();
            RenderSystem.applyModelViewMatrix();
        }
    }
}
