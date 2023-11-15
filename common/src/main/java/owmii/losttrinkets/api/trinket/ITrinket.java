package owmii.losttrinkets.api.trinket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public interface ITrinket extends IItemProvider {
    default void addTrinketDescription(ItemStack stack, List<ITextComponent> lines) {
        ResourceLocation key = Registry.ITEM.getKey(stack.getItem());
        lines.add(new TranslationTextComponent(Util.makeTranslationKey("info", key)).mergeStyle(TextFormatting.GRAY));
    }

    void onActivated(World world, BlockPos pos, PlayerEntity player);

    void onDeactivated(World world, BlockPos pos, PlayerEntity player);

    Rarity getRarity();

    boolean isUnlockable();

    void setUnlockable(boolean unlockable);
}
