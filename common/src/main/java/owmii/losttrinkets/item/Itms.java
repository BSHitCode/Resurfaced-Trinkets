package owmii.losttrinkets.item;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.BiFunction;

import me.shedaniel.architectury.registry.DeferredRegister;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.trinkets.*;

public class Itms {
    static final DeferredRegister<Item> ITEMS = DeferredRegister.create(LostTrinkets.MOD_ID, Registry.ITEM_KEY);

    public static void setup() {
        ITEMS.register();
    }

    private static Item.Properties properties() {
        return new Item.Properties().group(ItemGroups.MAIN);
    }

    private static <I extends Item> Supplier<I> register(String name, Function<Item.Properties, I> func) {
        return ITEMS.register(name, () -> func.apply(properties()));
    }

    private static <I extends Item> Supplier<I> register(String name, BiFunction<Rarity, Item.Properties, I> func, Rarity rarity) {
        return ITEMS.register(name, () -> func.apply(rarity, properties()));
    }

    private static <I extends Item> Supplier<I> register(String name, BiFunction<Rarity, Item.Properties, I> func, Rarity rarity, Consumer<I> consumer) {
        return ITEMS.register(name, () -> {
            I res = func.apply(rarity, properties());
            consumer.accept(res);
            return res;
        });
    }

    public static final Supplier<Trinket<?>> PIGGY = register("piggy", Trinket::new, Rarity.COMMON);
    public static final Supplier<CreepoTrinket> CREEPO = register("creepo", CreepoTrinket::new, Rarity.COMMON);
    public static final Supplier<HorseshoeTrinket> HORSESHOE = register("horseshoe", HorseshoeTrinket::new, Rarity.COMMON);
    public static final Supplier<ButchersCleaverTrinket> BUTCHERS_CLEAVER = register("butchers_cleaver", ButchersCleaverTrinket::new, Rarity.COMMON);
    public static final Supplier<SlingshotTrinket> SLINGSHOT = register("slingshot", SlingshotTrinket::new, Rarity.COMMON);
    public static final Supplier<MagnetoTrinket> MAGNETO = register("magneto", MagnetoTrinket::new, Rarity.COMMON);

    public static final Supplier<RockCandyTrinket> ROCK_CANDY = register("rock_candy", RockCandyTrinket::new, Rarity.UNCOMMON);
    public static final Supplier<LunchBagTrinket> LUNCH_BAG = register("lunch_bag", LunchBagTrinket::new, Rarity.UNCOMMON);
    public static final Supplier<LuckCoinTrinket> LUCK_COIN = register("luck_coin", LuckCoinTrinket::new, Rarity.UNCOMMON);
    public static final Supplier<MinersPickTrinket> MINERS_PICK = register("miners_pick", MinersPickTrinket::new, Rarity.UNCOMMON);
    public static final Supplier<ThaCloudTrinket> THA_CLOUD = register("tha_cloud", ThaCloudTrinket::new, Rarity.UNCOMMON);
    public static final Supplier<TurtleShellTrinket> TURTLE_SHELL = register("turtle_shell", TurtleShellTrinket::new, Rarity.UNCOMMON);
    public static final Supplier<IceShardTrinket> ICE_SHARD = register("ice_shard", IceShardTrinket::new, Rarity.UNCOMMON);

    public static final Supplier<Trinket<?>> EMPTY_AMULET = register("empty_amulet", Trinket::new, Rarity.RARE);
    public static final Supplier<Trinket<?>> THA_SPIDER = register("tha_spider", Trinket::new, Rarity.RARE);
    public static final Supplier<Trinket<?>> GLASS_SHARD = register("glass_shard", Trinket::new, Rarity.RARE);
    public static final Supplier<BlazeHeartTrinket> BLAZE_HEART = register("blaze_heart", BlazeHeartTrinket::new, Rarity.RARE);
    public static final Supplier<ThaGhostTrinket> THA_GHOST = register("tha_ghost", ThaGhostTrinket::new, Rarity.RARE);
    public static final Supplier<TrebleHooksTrinket> TREBLE_HOOKS = register("treble_hooks", TrebleHooksTrinket::new, Rarity.RARE);
    public static final Supplier<ThaWizardTrinket> THA_WIZARD = register("tha_wizard", ThaWizardTrinket::new, Rarity.RARE);
    public static final Supplier<ThaBatTrinket> THA_BAT = register("tha_bat", ThaBatTrinket::new, Rarity.RARE);
    public static final Supplier<Trinket<?>> BLANK_EYES = register("blank_eyes", Trinket::new, Rarity.RARE);
    public static final Supplier<BigFootTrinket> BIG_FOOT = register("big_foot", BigFootTrinket::new, Rarity.RARE);

    public static final Supplier<Trinket<?>> BOOK_O_ENCHANTING = register("book_o_enchanting", Trinket::new, Rarity.MASTER);
    public static final Supplier<WarmVoidTrinket> WARM_VOID = register("warm_void", WarmVoidTrinket::new, Rarity.MASTER);
    public static final Supplier<GoldenMelonTrinket> GOLDEN_MELON = register("golden_melon", GoldenMelonTrinket::new, Rarity.MASTER);
    public static final Supplier<WitherNailTrinket> WITHER_NAIL = register("wither_nail", WitherNailTrinket::new, Rarity.MASTER);
    public static final Supplier<SerpentToothTrinket> SERPENT_TOOTH = register("serpent_tooth", SerpentToothTrinket::new, Rarity.MASTER);
    public static final Supplier<MadPiggyTrinket> MAD_PIGGY = register("mad_piggy", MadPiggyTrinket::new, Rarity.MASTER);
    public static final Supplier<Trinket<?>> MINDS_EYE = register("minds_eye", Trinket::new, Rarity.MASTER);
    public static final Supplier<GoldenSwatterTrinket> GOLDEN_SWATTER = register("golden_swatter", GoldenSwatterTrinket::new, Rarity.MASTER);
    public static final Supplier<StickyMindTrinket> STICKY_MIND = register("sticky_mind", StickyMindTrinket::new, Rarity.MASTER);
    public static final Supplier<FireMindTrinket> FIRE_MIND = register("fire_mind", FireMindTrinket::new, Rarity.MASTER);
    public static final Supplier<Trinket<?>> THA_GOLEM = register("tha_golem", Trinket::new, Rarity.MASTER, (trinket) -> trinket.add(Attributes.KNOCKBACK_RESISTANCE, "afb13d18-56f2-4e1f-8281-8cc7e3005eef", 1.0D));
    public static final Supplier<DragonBreathTrinket> DRAGON_BREATH = register("dragon_breath", DragonBreathTrinket::new, Rarity.MASTER);

    public static final Supplier<Trinket<?>> KARMA = register("karma", Trinket::new, Rarity.ELITE);
    public static final Supplier<DarkDaggerTrinket> DARK_DAGGER = register("dark_dagger", DarkDaggerTrinket::new, Rarity.ELITE);
    public static final Supplier<StarfishTrinket> STARFISH = register("starfish", StarfishTrinket::new, Rarity.ELITE);
    public static final Supplier<DropSpindleTrinket> DROP_SPINDLE = register("drop_spindle", DropSpindleTrinket::new, Rarity.ELITE);
    public static final Supplier<EmberTrinket> EMBER = register("ember", EmberTrinket::new, Rarity.ELITE);
    public static final Supplier<TeaLeafTrinket> TEA_LEAF = register("tea_leaf", TeaLeafTrinket::new, Rarity.ELITE);
    public static final Supplier<CoffeeBeanTrinket> COFFEE_BEAN = register("coffee_bean", CoffeeBeanTrinket::new, Rarity.ELITE);
    public static final Supplier<OxalisTrinket> OXALIS = register("oxalis", OxalisTrinket::new, Rarity.ELITE);
    public static final Supplier<GoldenSkullTrinket> GOLDEN_SKULL = register("golden_skull", GoldenSkullTrinket::new, Rarity.ELITE);

    public static final Supplier<DarkEggTrinket> DARK_EGG = register("dark_egg", DarkEggTrinket::new, Rarity.EPIC);
    public static final Supplier<PillowOfSecretsTrinket> PILLOW_OF_SECRETS = register("pillow_of_secrets", PillowOfSecretsTrinket::new, Rarity.EPIC);
    public static final Supplier<ThaSpiritTrinket> THA_SPIRIT = register("tha_spirit", ThaSpiritTrinket::new, Rarity.EPIC);
    public static final Supplier<MirrorShardTrinket> MIRROR_SHARD = register("mirror_shard", MirrorShardTrinket::new, Rarity.EPIC);
    public static final Supplier<MossyRingTrinket> MOSSY_RING = register("mossy_ring", MossyRingTrinket::new, Rarity.EPIC);
    public static final Supplier<MossyBeltTrinket> MOSSY_BELT = register("mossy_belt", MossyBeltTrinket::new, Rarity.EPIC);
    public static final Supplier<TreasureRingTrinket> TREASURE_RING = register("treasure_ring", TreasureRingTrinket::new, Rarity.EPIC);
    public static final Supplier<OctopickTrinket> OCTOPICK = register("octopick", OctopickTrinket::new, Rarity.EPIC);
    public static final Supplier<Trinket<?>> SILVER_NAIL = register("silver_nail", Trinket::new, Rarity.EPIC);
    public static final Supplier<Trinket<?>> GLORY_SHARDS = register("glory_shards", Trinket::new, Rarity.EPIC);

    public static final Supplier<Trinket<?>> ASH_GLOVES = register("ash_gloves", Trinket::new, Rarity.LEGENDARY, (trinket) -> trinket.add(Attributes.ATTACK_SPEED, "1a71bd06-0d8b-459e-b961-fbd992d61c5d", 1024.0D));
    public static final Supplier<RubyHeartTrinket> RUBY_HEART = register("ruby_heart", RubyHeartTrinket::new, Rarity.LEGENDARY);
    public static final Supplier<Trinket<?>> GOLDEN_HORSESHOE = register("golden_horseshoe", Trinket::new, Rarity.LEGENDARY);
    public static final Supplier<Trinket<?>> GOLDEN_TOOTH = register("golden_tooth", Trinket::new, Rarity.LEGENDARY);
    public static final Supplier<Trinket<?>> BROKEN_HEART_1 = register("broken_heart_1", Trinket::new, Rarity.LEGENDARY, (trinket) -> trinket.add(Attributes.MAX_HEALTH, "092962d0-2711-48e0-9a84-f768ea4aeeb2", 4.0D));
    public static final Supplier<Trinket<?>> BROKEN_HEART_2 = register("broken_heart_2", Trinket::new, Rarity.LEGENDARY, (trinket) -> trinket.add(Attributes.MAX_HEALTH, "bf4f459a-d398-4cc1-a146-9d3828f2201a", 4.0D));
    public static final Supplier<Trinket<?>> BROKEN_HEART_3 = register("broken_heart_3", Trinket::new, Rarity.LEGENDARY, (trinket) -> trinket.add(Attributes.MAX_HEALTH, "cb979db5-2f24-40b5-b2ef-4b7d29491ef4", 4.0D));
    public static final Supplier<Trinket<?>> BROKEN_HEART_4 = register("broken_heart_4", Trinket::new, Rarity.LEGENDARY, (trinket) -> trinket.add(Attributes.MAX_HEALTH, "1816e016-b569-4258-889e-d45829628248", 4.0D));
    public static final Supplier<Trinket<?>> BROKEN_HEART_5 = register("broken_heart_5", Trinket::new, Rarity.LEGENDARY, (trinket) -> trinket.add(Attributes.MAX_HEALTH, "a3fee661-c9e0-40d9-8386-ac245576bed0", 4.0D));
    public static final Supplier<OctopusLegTrinket> OCTOPUS_LEG = register("octopus_leg", OctopusLegTrinket::new, Rarity.LEGENDARY);
    public static final Supplier<MagicalHerbsTrinket> MAGICAL_HERBS = register("magical_herbs", MagicalHerbsTrinket::new, Rarity.LEGENDARY);
    public static final Supplier<MagicalFeathersTrinket> MAGICAL_FEATHERS = register("magical_feathers", MagicalFeathersTrinket::new, Rarity.LEGENDARY);
    public static final Supplier<MadAuraTrinket> MAD_AURA = register("mad_aura", MadAuraTrinket::new, Rarity.LEGENDARY);
    public static final Supplier<Trinket<?>> BROKEN_TOTEM = register("broken_totem", Trinket::new, Rarity.LEGENDARY);

    public static final Supplier<Item> TREASURE_BAG = register("treasure_bag", TreasureBagItem::new);
}
