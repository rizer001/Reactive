package org.reactive.config.modules.opt;

import org.reactive.config.ConfigModules;
import org.reactive.config.EnumConfigCategory;
import org.reactive.config.annotations.Experimental;

public class OptimizeWaypoint extends ConfigModules {
    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".optimize-waypoint";
    }

    @Experimental
    public static boolean enabled = false;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath(), enabled);
    }
}
