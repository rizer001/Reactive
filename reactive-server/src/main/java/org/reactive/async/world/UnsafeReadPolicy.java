package org.reactive.async.world;

import org.reactive.config.ReactiveConfig;

import java.util.Locale;

public enum UnsafeReadPolicy {
    STRICT,
    BUFFERED,
    DISABLED;

    public static UnsafeReadPolicy fromString(String readPolicy) {
        try {
            return valueOf(readPolicy.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            ReactiveConfig.LOGGER.warn("Invalid unsafe read policy: {}, falling back to {}.", readPolicy, DISABLED.toString());
            return DISABLED;
        }
    }
}
