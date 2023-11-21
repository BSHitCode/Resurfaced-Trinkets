package owmii.losttrinkets.config;

import static owmii.losttrinkets.LostTrinkets.LOGGER;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import me.shedaniel.architectury.utils.GameInstance;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.Toml;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.text.TranslatableText;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.handler.UnlockManager;

@Config(name = "losttrinkets")
public class SunkenTrinketsConfig implements ConfigData {
    public static final Marker MARKER = new MarkerManager.Log4jMarker("Config");

    @Comment("Set to false to disable the default way of unlocking trinkets.")
    public boolean unlockEnabled = true;

    @Comment("Range: 0 ~ 1728000")
    public long unlockCooldown = 2400L;

    public List<String> blackList = new ArrayList<>();
    public List<String> nonRandom = new ArrayList<>();

    public int startSlots = 1;
    public int maxSlots = 40;
    public int slotCost = 15;
    public int slotUpFactor = 3;

    public static final int killingDefault = 120;
    public static final int bossKillingDefault = 10;
    public static final int farmingDefault = 140;
    public static final int oresMiningDefault = 100;
    public static final int tradingDefault = 30;
    public static final int woodCuttingDefault = 170;

    public boolean killingUnlockEnabled = true;
    public int killing = killingDefault;
    public boolean bossKillingUnlockEnabled = true;
    public int bossKilling = bossKillingDefault;
    public boolean farmingUnlockEnabled = true;
    public int farming = farmingDefault;
    public boolean oresMiningUnlockEnabled = true;
    public int oresMining = oresMiningDefault;
    public boolean tradingUnlockEnabled = true;
    public int trading = tradingDefault;
    public boolean woodCuttingUnlockEnabled = true;
    public int woodCutting = woodCuttingDefault;

    public void validatePostLoad() throws ValidationException {
        if (GameInstance.getServer() != null) {
            // refresh only if a server is running...
            UnlockManager.refresh();
        }
    }

    public int calcCost(int currentSlosts) {
        if (currentSlosts >= this.maxSlots) {
            return -1;
        }
        int startSlots = this.startSlots;
        if (currentSlosts < startSlots) {
            return 0;
        }
        return this.slotCost + ((currentSlosts - startSlots) * this.slotUpFactor);
    }

    private static TranslatableText tr(final String key) {
        return new TranslatableText("gui.losttrinkets.config." + key);
    }

    private static SubCategoryBuilder makeUnlocksSubcategory(
        SunkenTrinketsConfig config,
        final ConfigEntryBuilder entryBuilder,
        final String name,
        final int defaultValue
    ) {
        final SubCategoryBuilder cat = entryBuilder.startSubCategory(tr(name + "Unlocks"));

        try {
            final String unlockEnabledName = name + "UnlockEnabled";
            final Field unlockEnabledField = SunkenTrinketsConfig.class.getDeclaredField(unlockEnabledName);
            cat.add(
                entryBuilder.startBooleanToggle(tr(unlockEnabledName + ".text"), Utils.getUnsafely(unlockEnabledField, config, true))
                    .setTooltip(tr(unlockEnabledName + ".tooltip"))
                    .setDefaultValue(true)
                    .setSaveConsumer((v) -> Utils.setUnsafely(unlockEnabledField, config, v))
                    .build()
            );

            final Field rarityField = SunkenTrinketsConfig.class.getDeclaredField(name);
            cat.add(
                entryBuilder.startIntField(tr(name + ".text"), Utils.getUnsafely(rarityField, config, defaultValue))
                    .setTooltip(tr(name + ".tooltip"))
                    .setDefaultValue(defaultValue).setMin(2).setMax(100000)
                    .setSaveConsumer((v) -> Utils.setUnsafely(rarityField, config, v))
                    .build()
            );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to serialize unlocks: " + name, e);
        }

        return cat;
    }

    public static ConfigBuilder builder() {
        SunkenTrinketsConfig config = LostTrinkets.CONFIG.getConfig();

        final ConfigBuilder builder = ConfigBuilder.create().setTitle(tr("title"));
        final ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        final ConfigCategory defaultCategory = builder.getOrCreateCategory(tr("category.default"));

        defaultCategory.addEntry(
            entryBuilder.startBooleanToggle(tr("unlockEnabled.text"), config.unlockEnabled)
                .setTooltip(tr("unlockEnabled.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer((v) -> config.unlockEnabled = v)
                .build()
        );

        defaultCategory.addEntry(
            entryBuilder.startLongField(tr("unlockCooldown.text"), config.unlockCooldown)
                .setTooltip(tr("unlockCooldown.tooltip"))
                .setDefaultValue(2400L).setMin(0L).setMax(1728000L)
                .setSaveConsumer((v) -> config.unlockCooldown = v)
                .build()
        );

        // blackList
        {
            final SubCategoryBuilder cat = entryBuilder.startSubCategory(tr("blackList.text"));
            cat.add(entryBuilder.startTextDescription(tr("blackList.desc")).build());
            cat.add(
                entryBuilder.startStrList(tr("blackList.label"), config.blackList)
                    .setDefaultValue(new ArrayList<>())
                    .setSaveConsumer((v) -> config.blackList = v)
                    .requireRestart()
                    .build()
            );
            defaultCategory.addEntry(cat.build());
        }

        // nonRandom
        {
            final SubCategoryBuilder cat = entryBuilder.startSubCategory(tr("nonRandom.text"));
            cat.add(entryBuilder.startTextDescription(tr("nonRandom.desc")).build());
            cat.add(
                entryBuilder.startStrList(tr("nonRandom.label"), config.nonRandom)
                    .setDefaultValue(new ArrayList<>())
                    .setSaveConsumer((v) -> config.nonRandom = v)
                    .requireRestart()
                    .build()
            );
            defaultCategory.addEntry(cat.build());
        }

        // Trinket_Slots
        {
            final SubCategoryBuilder cat = entryBuilder.startSubCategory(tr("trinketSlots"));
            cat.add(
                entryBuilder.startIntSlider(tr("startSlots.text"), config.startSlots, 0, 40)
                    .setTooltip(tr("startSlots.tooltip"))
                    .setDefaultValue(1)
                    .setSaveConsumer((v) -> config.startSlots = v)
                    .build()
            );
            cat.add(
                entryBuilder.startIntSlider(tr("maxSlots.text"), config.maxSlots, 1, 40)
                    .setTooltip(tr("maxSlots.tooltip"))
                    .setDefaultValue(40)
                    .setSaveConsumer((v) -> config.maxSlots = v)
                    .build()
            );
            cat.add(
                entryBuilder.startIntSlider(tr("slotCost.text"), config.slotCost, 0, 1000)
                    .setTooltip(tr("slotCost.tooltip"))
                    .setDefaultValue(15)
                    .setSaveConsumer((v) -> config.slotCost = v)
                    .build()
            );
            cat.add(
                entryBuilder.startIntSlider(tr("slotUpFactor.text"), config.slotUpFactor, 0, 1000)
                    .setTooltip(tr("slotUpFactor.tooltip"))
                    .setDefaultValue(3)
                    .setSaveConsumer((v) -> config.slotUpFactor = v)
                    .build()
            );
            defaultCategory.addEntry(cat.build());
        }

        defaultCategory.addEntry(makeUnlocksSubcategory(config, entryBuilder, "killing", killingDefault).build());
        defaultCategory.addEntry(makeUnlocksSubcategory(config, entryBuilder, "bossKilling", bossKillingDefault).build());
        defaultCategory.addEntry(makeUnlocksSubcategory(config, entryBuilder, "farming", farmingDefault).build());
        defaultCategory.addEntry(makeUnlocksSubcategory(config, entryBuilder, "oresMining", oresMiningDefault).build());
        defaultCategory.addEntry(makeUnlocksSubcategory(config, entryBuilder, "trading", tradingDefault).build());
        defaultCategory.addEntry(makeUnlocksSubcategory(config, entryBuilder, "woodCutting", woodCuttingDefault).build());

        builder.setSavingRunnable(() -> {
            LostTrinkets.CONFIG.save();
        });

        return builder;
    }

    public static class Serializer implements ConfigSerializer<SunkenTrinketsConfig> {
        @Override
        public SunkenTrinketsConfig createDefault() {
            return new SunkenTrinketsConfig();
        }

        private Path getConfigPath() {
            // Use the same path as when using Forge's config API, so we can stay compatible
            return Utils.getConfigFolder().resolve("losttrinkets").resolve("general_common.toml");
            //return Utils.getConfigFolder().resolve("losttrinkets.toml");
        }

        private static void deserializeUnlocks(SunkenTrinketsConfig config, final Toml toml, final String tableName, final String name, final int defaultValue) {
            if (!toml.containsTable(tableName)) {
                return;
            }

            final Toml table = toml.getTable(tableName);

            try {
                final String unlockEnabledName = name + "UnlockEnabled";
                final Field unlockEnabledField = SunkenTrinketsConfig.class.getDeclaredField(unlockEnabledName);
                if (table.contains(unlockEnabledName)) {
                    Utils.setUnsafely(unlockEnabledField, config, table.getBoolean(unlockEnabledName));
                }

                final Field rarityField = SunkenTrinketsConfig.class.getDeclaredField(name);
                if (table.contains(name)) {
                    Utils.setUnsafely(rarityField, config, (int) (long) table.getLong(name));
                }
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Failed to serialize unlocks: " + name, e);
            }
        }

        @Override
        public SunkenTrinketsConfig deserialize() throws SerializationException {
            Path configPath = this.getConfigPath();
            if (Files.exists(configPath, new LinkOption[0])) {
                final SunkenTrinketsConfig config = this.createDefault();
                final Toml toml = (new Toml()).read(configPath.toFile());

                config.unlockEnabled = toml.getBoolean("unlockEnabled", true);
                if (toml.containsPrimitive("unlockCooldown")) {
                    config.unlockCooldown = toml.getLong("unlockCooldown");
                }
                if (toml.contains("blackList")) {
                    config.blackList = toml.getList("blackList");
                }
                if (toml.contains("nonRandom")) {
                    config.nonRandom = toml.getList("nonRandom");
                }

                if (toml.containsTable("Trinket_Slots")) {
                    Toml tab = toml.getTable("Trinket_Slots");
                    config.slotUpFactor = (int) (long) tab.getLong("slotUpFactor");
                    config.maxSlots = (int) (long) tab.getLong("maxSlots");
                    config.startSlots = (int) (long) tab.getLong("startSlots");
                    config.slotCost = (int) (long) tab.getLong("slotCost");
                }

                deserializeUnlocks(config, toml, "Killing_Unlocks", "killing", killingDefault);
                deserializeUnlocks(config, toml, "Bosses_Killing_Unlocks", "bossKilling", bossKillingDefault);
                deserializeUnlocks(config, toml, "Farming_Unlocks", "farming", farmingDefault);
                deserializeUnlocks(config, toml, "Ores_Mining_Unlocks", "oresMining", oresMiningDefault);
                deserializeUnlocks(config, toml, "Trading_Unlocks", "trading", tradingDefault);
                deserializeUnlocks(config, toml, "Wood_Cutting_Unlocks", "woodCutting", woodCuttingDefault);

                return config;
            } else {
                return this.createDefault();
            }
        }

        private static Map<String, Object> serializeUnlocks(final SunkenTrinketsConfig config, final String name, final int defaultValue) {
            Map<String, Object> data = new LinkedHashMap<>();
            try {
                final String unlockEnabledName = name + "UnlockEnabled";
                final Field unlockEnabledField = SunkenTrinketsConfig.class.getDeclaredField(unlockEnabledName);
                data.put(unlockEnabledName, Utils.getUnsafely(unlockEnabledField, config, true));

                final Field rarityField = SunkenTrinketsConfig.class.getDeclaredField(name);
                data.put(name, Utils.getUnsafely(rarityField, config, defaultValue));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Failed to serialize unlocks: " + name, e);
            }
            return data;
        }

        @Override
        public void serialize(final SunkenTrinketsConfig config) throws SerializationException {
            Path configPath = this.getConfigPath();
            try {
                Files.createDirectories(configPath.getParent());

                Map<String, Object> configData = new LinkedHashMap<>();
                configData.put("unlockEnabled", config.unlockEnabled);
                configData.put("unlockCooldown", config.unlockCooldown);
                configData.put("blackList", config.blackList);
                configData.put("nonRandom", config.nonRandom);

                {
                    Map<String, Object> data = new LinkedHashMap<>();
                    data.put("slotUpFactor", config.slotUpFactor);
                    data.put("maxSlots", config.maxSlots);
                    data.put("startSlots", config.startSlots);
                    data.put("slotCost", config.slotCost);
                    configData.put("Trinket_Slots", data);
                }

                configData.put("Killing_Unlocks", serializeUnlocks(config, "killing", killingDefault));
                configData.put("Bosses_Killing_Unlocks", serializeUnlocks(config, "bossKilling", bossKillingDefault));
                configData.put("Farming_Unlocks", serializeUnlocks(config, "farming", farmingDefault));
                configData.put("Ores_Mining_Unlocks", serializeUnlocks(config, "oresMining", oresMiningDefault));
                configData.put("Trading_Unlocks", serializeUnlocks(config, "trading", tradingDefault));
                configData.put("Wood_Cutting_Unlocks", serializeUnlocks(config, "woodCutting", woodCuttingDefault));

                TomlWriter writer = new TomlWriter();
                writer.write(configData, configPath.toFile());
            } catch (IOException var4) {
                throw new ConfigSerializer.SerializationException(var4);
            }
        }
    }

    public static ConfigHolder<SunkenTrinketsConfig> register() {
        return AutoConfig.register(
            SunkenTrinketsConfig.class,
            (cfg, cfgClass) -> {
                return new SunkenTrinketsConfig.Serializer();
            }
        );
    }

}
