package org.reactive.config.modules.opt;

import org.reactive.config.ConfigModules;
import org.reactive.config.EnumConfigCategory;
import org.reactive.config.annotations.Experimental;

public class OptimizeNoActionTime extends ConfigModules {
    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".optimize-no-action-time";
    }

    @Experimental
    public static boolean disableLightCheck = false;

    @Override
    public void onLoaded() {
        disableLightCheck = config.getBoolean(getBasePath() + ".disable-light-check", disableLightCheck);
    }
}
