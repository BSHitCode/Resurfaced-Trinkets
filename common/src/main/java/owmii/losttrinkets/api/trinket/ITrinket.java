package owmii.losttrinkets.api.trinket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public interface ITrinket extends ItemConvertible {
    default void addTrinketDescription(ItemStack stack, List<Text> lines) {
        Identifier key = Registry.ITEM.getId(stack.getItem());
        lines.add(new TranslatableText(Util.createTranslationKey("info", key)).formatted(Formatting.GRAY));
    }

    void onActivated(World world, BlockPos pos, PlayerEntity player);

    void onDeactivated(World world, BlockPos pos, PlayerEntity player);

    Rarity getRarity();

    boolean isUnlockable();

    void setUnlockable(boolean unlockable);
}
