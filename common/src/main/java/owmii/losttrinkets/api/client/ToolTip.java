package owmii.losttrinkets.api.client;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

@FunctionalInterface
public interface ToolTip {
    void apply(ItemStack stack, @Nullable World world, List<Text> tooltip);
}
