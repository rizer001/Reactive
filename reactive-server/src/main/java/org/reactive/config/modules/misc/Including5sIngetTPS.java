package org.reactive.config.modules.misc;

import org.reactive.config.ConfigModules;
import org.reactive.config.EnumConfigCategory;

public class Including5sIngetTPS extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.MISC.getBaseKeyName();
    }

    public static boolean enabled = true;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath() + ".including-5s-in-get-tps", enabled);
    }
}
