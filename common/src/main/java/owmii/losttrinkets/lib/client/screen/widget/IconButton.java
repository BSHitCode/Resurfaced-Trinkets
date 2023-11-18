package owmii.losttrinkets.lib.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import owmii.losttrinkets.lib.client.screen.Texture;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IconButton extends ButtonWidget {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    private Consumer<List<Text>> tooltipConsumer = stringList -> {
    };
    private Screen screen;
    private Texture texture;
    private Texture hovering;
    private ItemStack stack;
    private float xOffset;
    private float yOffset;

    @Nullable
    private SoundEvent sound;

    public IconButton(int x, int y, Text text, Texture texture, PressAction onPress, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, Texture.EMPTY, text, onPress, screen);
    }

    public IconButton(int x, int y, Text text, Texture texture, PressAction onPress, Texture hovering, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, hovering, text, onPress, screen);
    }

    public IconButton(int x, int y, Texture texture, PressAction onPress, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, Texture.EMPTY, new LiteralText(""), onPress, screen);
    }

    public IconButton(int x, int y, Texture texture, Texture hovering, PressAction onPress, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, hovering, new LiteralText(""), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, PressAction onPress, Screen screen) {
        this(x, y, stack, texture, Texture.EMPTY, new LiteralText(""), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, Texture hovering, PressAction onPress, Screen screen) {
        this(x, y, stack, texture, hovering, new LiteralText(""), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, Texture hovering, Text text, PressAction onPress, Screen screen) {
        super(x, y, texture.getWidth(), texture.getHeight(), text, onPress);
        this.texture = texture;
        this.screen = screen;
        this.hovering = hovering;
        this.stack = stack;
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float pt) {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (this.hovered && !this.hovering.isEmpty()) {
                this.hovering.draw(matrix, this.x, this.y);
            } else {
                this.texture.draw(matrix, this.x, this.y);
            }
            TextRenderer f = this.mc.textRenderer;
            String s = getMessage().getString();
            if (!s.isEmpty()) {
                int width = f.getWidth(s);
                TextColor c = getMessage().getStyle().getColor();
                int color = c == null ? 0x555555 : c.getRgb();
                f.draw(matrix, s, this.xOffset + this.x + 0.5F + this.width / 2.0F - width / 2.0F, this.yOffset + this.y + this.height / 2.0F - 4, color);
            }
            if (!this.stack.isEmpty()) {
                RenderSystem.pushMatrix();
                MinecraftClient mc = MinecraftClient.getInstance();
                RenderSystem.translated(this.xOffset + this.x - 8.0D + this.width / 2.0F, this.yOffset + this.y - 8.0D + this.height / 2.0F, 0.0F);
                mc.getItemRenderer().renderInGuiWithOverrides(this.stack, 0, 0);
                RenderSystem.popMatrix();
            }
        }
    }

    @Override
    public void renderToolTip(MatrixStack matrix, int mouseX, int mouseY) {
        List<Text> tooltip = new ArrayList<>();
        this.tooltipConsumer.accept(tooltip);
        if (!tooltip.isEmpty()) {
            this.screen.renderTooltip(matrix, tooltip, mouseX, mouseY);
        }
    }

    public void blit(MatrixStack matrix, Texture texture, int x, int y) {
        bindTexture(texture.getLocation());
        drawTexture(matrix, x, y, texture.getU(), texture.getV(), texture.getWidth(), texture.getHeight());
    }

    public void bindTexture(Identifier guiTexture) {
        this.mc.getTextureManager().bindTexture(guiTexture);
    }

    public Screen getScreen() {
        return this.screen;
    }

    public IconButton setScreen(Screen screen) {
        this.screen = screen;
        return this;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public IconButton setTexture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public Texture getHovering() {
        return this.hovering;
    }

    public IconButton setHovering(Texture hovering) {
        this.hovering = hovering;
        return this;
    }

    public Consumer<List<Text>> getTooltip() {
        return this.tooltipConsumer;
    }

    public IconButton setTooltip(Consumer<List<Text>> tooltipConsumer) {
        this.tooltipConsumer = tooltipConsumer;
        return this;
    }

    public IconButton setStackInSlot(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public IconButton xOffset(float xOffset) {
        this.xOffset = xOffset;
        return this;
    }

    public IconButton yOffset(float yOffset) {
        this.yOffset = yOffset;
        return this;
    }

    @Override
    public void playDownSound(SoundManager handler) {
        if (this.sound != null) {
            handler.play(PositionedSoundInstance.master(this.sound, 1.0F));
        }
    }

    public IconButton setClickSound() {
        this.sound = SoundEvents.UI_BUTTON_CLICK;
        return this;
    }

    public IconButton setSound(@Nullable SoundEvent sound) {
        this.sound = sound;
        return this;
    }

    public static final IconButton EMPTY = new IconButton(0, 0, Texture.EMPTY, b -> {
    }, new ChatScreen(""));
}
