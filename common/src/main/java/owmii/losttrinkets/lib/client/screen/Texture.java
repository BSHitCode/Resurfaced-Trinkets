package owmii.losttrinkets.lib.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import owmii.losttrinkets.LostTrinkets;

public class Texture extends DrawableHelper {
    public static final Texture EMPTY = new Texture(
        new Identifier(LostTrinkets.MOD_ID, "textures/gui/empty.png"), 0, 0, 0, 0
    );

    private final Identifier location;
    private final int width, height;
    private final int u, v;
    private final int tw, th;

    public Texture(Identifier location, int width, int height) {
        this(location, width, height, 0, 0, width, height);
    }

    public Texture(Identifier location, int width, int height, int u, int v) {
        this(location, width, height, u, v, 256, 256);
    }

    public Texture(Identifier location, int width, int height, int u, int v, int dim) {
        this(location, width, height, u, v, dim, dim);
    }

    public Texture(Identifier location, int width, int height, int u, int v, int tw, int th) {
        this.location = location;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.tw = tw;
        this.th = th;
    }

    public void drawScalableW(MatrixStack matrix, float size, int x, int y) {
        scaleW((int) (size * this.width)).draw(matrix, x, y);
    }

    public void drawScalableH(MatrixStack matrix, float size, int x, int y) {
        int i = (int) (size * this.height);
        scaleH(i).moveV(this.height - i).draw(matrix, x, y + this.height - i);
    }

    public void draw(MatrixStack matrix, int x, int y) {
        if (!isEmpty()) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, getLocation());
            drawTexture(matrix, x, y, getU(), getV(), getWidth(), getHeight(), this.tw, this.th);
        }
    }

    public Texture addW(int width) {
        return scaleW(this.width + width);
    }

    public Texture addH(int height) {
        return scaleH(this.height + height);
    }

    public Texture remW(int width) {
        return scaleW(this.width - width);
    }

    public Texture remH(int height) {
        return scaleH(this.height - height);
    }

    public Texture scaleW(int width) {
        return scale(width, this.height);
    }

    public Texture scaleH(int height) {
        return scale(this.width, height);
    }

    public Texture scale(int width, int height) {
        return new Texture(this.location, width, height, this.u, this.v);
    }

    public Texture moveU(int u) {
        return move(u, 0);
    }

    public Texture moveV(int v) {
        return move(0, v);
    }

    public Texture move(int u, int v) {
        return new Texture(this.location, this.width, this.height, this.u + u, this.v + v);
    }

    public Identifier getLocation() {
        return this.location;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getU(int i) {
        return this.u + i;
    }

    public int getV(int i) {
        return this.v + i;
    }

    public int getU() {
        return this.u;
    }

    public int getV() {
        return this.v;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isMouseOver(int x, int y, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
    }
}
