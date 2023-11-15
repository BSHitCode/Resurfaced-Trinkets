package owmii.losttrinkets.network;

import owmii.losttrinkets.forge.LostTrinketsForge;
import owmii.losttrinkets.network.packet.*;

public class Packets {
    public static void register() {
        LostTrinketsForge.NET.register(new SyncDataPacket());
        LostTrinketsForge.NET.register(new SetActivePacket());
        LostTrinketsForge.NET.register(new SetInactivePacket());
        LostTrinketsForge.NET.register(new UnlockSlotPacket());
        LostTrinketsForge.NET.register(new TrinketUnlockedPacket());
        LostTrinketsForge.NET.register(new SyncFlyPacket());
        LostTrinketsForge.NET.register(new MagnetoPacket());
    }
}
