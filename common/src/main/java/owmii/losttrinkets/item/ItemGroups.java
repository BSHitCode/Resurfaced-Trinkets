package owmii.losttrinkets.item;

import me.shedaniel.architectury.registry.CreativeTabs;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import owmii.losttrinkets.LostTrinkets;

public class ItemGroups {
    public static final ItemGroup MAIN = CreativeTabs.create(
        new Identifier(LostTrinkets.MOD_ID, "tab"),
        () -> new ItemStack(Itms.CREEPO.get())
    );
}
