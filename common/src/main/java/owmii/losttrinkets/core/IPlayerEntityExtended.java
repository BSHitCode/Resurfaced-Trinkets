package owmii.losttrinkets.core;

import owmii.losttrinkets.api.player.PlayerData;

public interface IPlayerEntityExtended {
    public static final String DATA_KEY = "sunken_trinkets";

    PlayerData sunkentrinkets_getPlayerData();
}
