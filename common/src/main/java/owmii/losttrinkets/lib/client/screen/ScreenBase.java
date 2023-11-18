package owmii.losttrinkets.lib.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ScreenBase extends Screen {
    public final MinecraftClient mc = MinecraftClient.getInstance();
    public int x, y, w, h;

    protected ScreenBase(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - this.w) / 2;
        this.y = (this.height - this.h) / 2;
    }

    @Override
    public void render(MatrixStack matrix, int mx, int my, float pt) {
        super.render(matrix, mx, my, pt);
        for (ClickableWidget widget : this.buttons) {
            if (widget.isHovered()) {
                widget.renderToolTip(matrix, mx, my);
                return;
            }
        }
    }

    @Override
    @SuppressWarnings("resource")
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            if (keyCode == 256 || MinecraftClient.getInstance().options.keyInventory.matchesKey(keyCode, scanCode)) {
                onClose();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public boolean isMouseOver(int x, int y, int w, int h, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h;
    }

    public <T extends ClickableWidget> T addButton2(T button) {
        return addButton(button);
    }
}
