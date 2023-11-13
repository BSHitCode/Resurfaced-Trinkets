package owmii.losttrinkets.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.losttrinkets.LostTrinkets;

public class Entities {
    static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, LostTrinkets.MOD_ID);

    public static void setup() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(bus);
    }

    public static final RegistryObject<EntityType<DarkVexEntity>> DARK_VEX = ENTITIES.register("dark_vex", () -> {
        return EntityType.Builder.create(DarkVexEntity::new, EntityClassification.CREATURE)
                .size(0.4F, 0.8F)
                .setUpdateInterval(3)
                .setTrackingRange(80)
                .setShouldReceiveVelocityUpdates(true)
                .build("dark_vex");
    });

    public static void register() {
        GlobalEntityTypeAttributes.put(DARK_VEX.get(), DarkVexEntity.getAttribute().create());
    }

    public static boolean isNonBossEntity(Entity entity) {
        // TODO: make a better check
        return entity.canChangeDimension();
    }
}
