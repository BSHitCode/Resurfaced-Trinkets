package owmii.losttrinkets.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.losttrinkets.LostTrinkets;

public class Sounds {
    static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LostTrinkets.MOD_ID);

    public static void setup() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        SOUND_EVENTS.register(bus);
    }

    public static final RegistryObject<SoundEvent> UNLOCK = register("unlock");

    static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(LostTrinkets.MOD_ID, name)));
    }
}
