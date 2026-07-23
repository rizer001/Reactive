package org.reactive.config.modules.opt;

import org.reactive.config.ConfigModules;
import org.reactive.config.EnumConfigCategory;
import org.reactive.config.annotations.Experimental;
import org.reactive.util.ReactiveConstants;

public class OptimizeDespawn extends ConfigModules {
    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".optimize-mob-despawn";
    }

    @Experimental
    public static boolean enabled = false;

    @Override
    public void onLoaded() {
        enabled = config.getBoolean(getBasePath(), enabled);
        if (enabled) {
            if (!ReactiveConstants.ENABLE_FMA) {
                LOGGER.info("NOTE: Recommend enabling FMA to work with optimize-mob-despawn.");
            }
        }
    }
}
