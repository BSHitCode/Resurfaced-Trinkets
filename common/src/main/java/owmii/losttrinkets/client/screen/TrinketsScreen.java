package owmii.losttrinkets.client.screen;

import com.google.common.collect.Lists;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
// import net.minecraftforge.fml.client.gui.GuiUtils;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.client.screen.widget.TrinketButton;
import owmii.losttrinkets.lib.client.screen.widget.IconButton;
import owmii.losttrinkets.network.Network;
import owmii.losttrinkets.network.packet.UnlockSlotPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TrinketsScreen extends AbstractLTScreen {
    private int x, y, columns = 10, rows = 4, btnDim = 28;

    public TrinketsScreen() {
        super(new TranslatableText("gui.losttrinkets.trinket.active"));
    }

    @Override
    protected void init() {
        super.init();
        if (this.mc.player != null) {
            this.x = this.width / 2 - this.columns * this.btnDim / 2;
            this.y = this.height / 2 - this.rows * this.btnDim / 2;
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(this.mc.player);
            int cost = LostTrinkets.config().calcCost(trinkets.getSlots());
            List<ITrinket> all = trinkets.getActiveTrinkets();
            label:
            for (int j1 = 0; j1 < this.rows; ++j1) {
                for (int j2 = 0; j2 < this.columns; ++j2) {
                    int i = j2 + j1 * this.columns;
                    if (i + 1 <= all.size()) {
                        ITrinket trinket = all.get(i);
                        addButton(new TrinketButton(this.x + j2 * this.btnDim, this.y + j1 * this.btnDim, Textures.TRINKET_ACTIVE_BG, trinket, button -> {
                            this.mc.setScreen(new TrinketOptionScreen(trinket, this));
                        }, (button, matrix, i1, i2) -> {
                            ItemStack stack = new ItemStack(trinket);
                            ArrayList<Text> list = Lists.newArrayList();
                            list.add(stack.getName());
                            trinket.addTrinketDescription(stack, list);
                            list.add(new TranslatableText("gui.losttrinkets.rarity." + trinket.getRarity().name().toLowerCase(Locale.ENGLISH)).formatted(Formatting.DARK_GRAY));
                            //GuiUtils.drawHoveringText(matrix, list, i1, i2, this.width, this.height, 240, this.font);
                            List<OrderedText> wrapped = list.stream()
                                .flatMap((t) -> this.textRenderer.wrapLines(t, Trinket.TOOLTIP_MAX_WIDTH).stream())
                                .collect(Collectors.toList());
                            this.renderOrderedTooltip(matrix, wrapped, i1, i2);
                        }));
                    } else {
                        boolean locked = i + 1 > trinkets.getSlots();
                        if (locked && cost < 0) {
                            break label;
                        }
                        addButton(new IconButton(this.x + j2 * this.btnDim, this.y + j1 * this.btnDim, locked ? Textures.TRINKET_BG_LOCKED : Textures.TRINKET_BG_ADD, button -> {
                            if (locked) {
                                Network.toServer(new UnlockSlotPacket());
                                setRefreshScreen(this);
                            } else {
                                this.mc.setScreen(new AvailableTrinketsScreen(this, 0));
                            }
                        }, this).setTooltip(tooltip -> {
                            if (locked) {
                                tooltip.add(new TranslatableText("gui.losttrinkets.trinket.slot.locked").formatted(Formatting.DARK_PURPLE));
                                if (!this.mc.player.isCreative()) {
                                    tooltip.add(new TranslatableText("gui.losttrinkets.trinket.slot.cost", cost).formatted(Formatting.DARK_GRAY));
                                }
                                tooltip.add(new LiteralText(""));
                                tooltip.add(new TranslatableText("gui.losttrinkets.trinket.slot.click.unlock").formatted(Formatting.GRAY));
                            } else {
                                tooltip.add(new TranslatableText("gui.losttrinkets.trinket.slot.click.add").formatted(Formatting.GRAY));
                            }
                        }));
                        if (locked) {
                            break label;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrix, int mx, int my, float pt) {
        renderBackground(matrix);
        super.render(matrix, mx, my, pt);
        String s = getTitle().getString();
        this.textRenderer.drawWithShadow(matrix, s, this.width / 2 - this.textRenderer.getWidth(s) / 2, this.y - 20, 0x999999);
    }
}
