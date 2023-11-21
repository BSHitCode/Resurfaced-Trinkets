package owmii.losttrinkets.client.screen;

import owmii.losttrinkets.client.handler.KeyHandler;
import owmii.losttrinkets.lib.client.screen.ScreenBase;

import javax.annotation.Nullable;
import net.minecraft.text.Text;

public class AbstractLTScreen extends ScreenBase {
    private boolean refresh;
    @Nullable
    private AbstractLTScreen toRefresh;

    protected AbstractLTScreen(Text title) {
        super(title);
    }

    @Override
    public void tick() {
        if (this.refresh && this.toRefresh != null) {
            this.mc.setScreen(this.toRefresh);
            this.refresh = false;
            this.toRefresh = null;
        }
    }

    public void refresh() {
        this.refresh = true;
    }

    public void setRefreshScreen(@Nullable AbstractLTScreen screen) {
        this.toRefresh = screen;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        if (super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
            return true;
        } else {
            if (KeyHandler.TRINKET_GUI.matchesKey(p_231046_1_, p_231046_2_)) {
                if (this.mc.player != null) {
                    onClose();
                }
                return true;
            }
        }
        return false;
    }
}
