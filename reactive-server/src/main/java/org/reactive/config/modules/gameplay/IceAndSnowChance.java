package org.reactive.config.modules.gameplay;

import org.reactive.config.ConfigModules;
import org.reactive.config.EnumConfigCategory;

public class IceAndSnowChance extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.GAMEPLAY.getBaseKeyName() + ".ice-and-snow-chance";
    }

    public static int iceAndSnowChance = 48;

    @Override
    public void onLoaded() {
        iceAndSnowChance = config.getInt(getBasePath(), iceAndSnowChance);
        if (iceAndSnowChance <= 0) {
            iceAndSnowChance = 48;
        }
    }
}
