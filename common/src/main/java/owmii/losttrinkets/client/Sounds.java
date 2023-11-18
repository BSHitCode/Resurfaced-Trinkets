package owmii.losttrinkets.client;

import java.util.function.Supplier;

import me.shedaniel.architectury.registry.DeferredRegister;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import owmii.losttrinkets.LostTrinkets;

public class Sounds {
    static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(LostTrinkets.MOD_ID, Registry.SOUND_EVENT_KEY);

    public static void setup() {
        SOUND_EVENTS.register();
    }

    public static final Supplier<SoundEvent> UNLOCK = register("unlock");

    static Supplier<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new Identifier(LostTrinkets.MOD_ID, name)));
    }
}
