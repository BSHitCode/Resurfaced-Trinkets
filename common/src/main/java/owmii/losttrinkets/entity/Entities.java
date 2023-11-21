package owmii.losttrinkets.entity;

import java.util.function.Supplier;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import owmii.losttrinkets.LostTrinkets;

public class Entities {
    static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(LostTrinkets.MOD_ID, Registry.ENTITY_TYPE_KEY);

    public static void setup() {
        ENTITIES.register();
    }

    public static final Supplier<EntityType<DarkVexEntity>> DARK_VEX = ENTITIES.register("dark_vex", () -> {
        return EntityType.Builder.create(DarkVexEntity::new, SpawnGroup.CREATURE)
                .setDimensions(0.4F, 0.8F)
                .trackingTickInterval(3)
                .maxTrackingRange(80)
                //.setShouldReceiveVelocityUpdates(true)
                .build("dark_vex");
    });

    public static boolean isNonBossEntity(Entity entity) {
        // TODO: make a better check
        return entity.canUsePortals();
    }
}
