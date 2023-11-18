package owmii.losttrinkets.api.trinket;

import com.google.common.collect.Maps;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.lib.client.util.MC;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Trinket<T extends Trinket<?>> extends Item implements ITrinket {
    private final Map<EntityAttribute, EntityAttributeModifier> attributes = Maps.newHashMap();
    private final Rarity rarity;
    protected boolean unlockable = true;

    public Trinket(Rarity rarity, Settings properties) {
        super(properties.maxCount(1));
        this.rarity = rarity;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (LostTrinketsAPI.get().unlock(player, this)) {
            ItemStack stack = player.getStackInHand(hand);
            if (!player.isCreative()) {
                stack.decrement(1);
            }
            return TypedActionResult.consume(stack);
        }
        return super.use(world, player, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext flag) {
        if (LostTrinketsAPI.get().isDisabled(this)) {
            tooltip.add(new TranslatableText("gui.losttrinkets.status.disabled").formatted(Formatting.DARK_RED));
        } else {
            PlayerEntity player = MC.player().orElse(null);
            if (player != null && LostTrinketsAPI.getTrinkets(player).has(this)) {
                tooltip.add(new TranslatableText("gui.losttrinkets.status.owned").formatted(Formatting.BLUE));
            } else if (LostTrinketsAPI.get().isNonRandom(this)) {
                tooltip.add(new TranslatableText("gui.losttrinkets.status.non_random").formatted(Formatting.DARK_GRAY));
            }
        }
        addTrinketDescription(stack, tooltip);
        tooltip.add(new TranslatableText("gui.losttrinkets.rarity." + getRarity().name().toLowerCase(Locale.ENGLISH)).formatted(Formatting.DARK_GRAY));
    }

    @Override
    public Text getName(ItemStack stack) {
        return super.getName(stack).shallowCopy().fillStyle(this.getRarity().getStyle());
    }

    @Override
    public void onActivated(World world, BlockPos pos, PlayerEntity player) {}

    @Override
    public void onDeactivated(World world, BlockPos pos, PlayerEntity player) {}

    @Override
    public Rarity getRarity() {
        return this.rarity;
    }

    @Override
    public boolean isUnlockable() {
        return this.unlockable;
    }

    public Trinket<T> noUnlock() {
        this.unlockable = false;
        return this;
    }

    @Override
    public void setUnlockable(boolean unlockable) {
        this.unlockable = unlockable;
    }

    @SuppressWarnings("unchecked")
    public T add(EntityAttribute attribute, String uuid, double amount) {
        EntityAttributeModifier attributemodifier = new EntityAttributeModifier(UUID.fromString(uuid), "Attribute", amount, EntityAttributeModifier.Operation.ADDITION);
        getAttributes().put(attribute, attributemodifier);
        return (T) this;
    }

    public void applyAttributes(PlayerEntity player) {
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : getAttributes().entrySet()) {
            EntityAttributeInstance attribute = player.getAttributeInstance(entry.getKey());
            if (attribute != null) {
                EntityAttributeModifier attributeModifier = entry.getValue();
                if (!attribute.hasModifier(attributeModifier)) {
                    attribute.addPersistentModifier(attributeModifier);
                }
            }
        }
    }

    public void removeAttributes(PlayerEntity player) {
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : getAttributes().entrySet()) {
            EntityAttributeInstance attribute = player.getAttributeInstance(entry.getKey());
            if (attribute != null) {
                attribute.removeModifier(entry.getValue());
            }
        }
    }

    public Map<EntityAttribute, EntityAttributeModifier> getAttributes() {
        return this.attributes;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
