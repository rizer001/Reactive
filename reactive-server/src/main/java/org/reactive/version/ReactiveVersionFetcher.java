package org.reactive.version;

import org.galemc.gale.version.AbstractPaperVersionFetcher;

public class ReactiveVersionFetcher extends AbstractPaperVersionFetcher {

    public static final String DOWNLOAD_PAGE = "https://www.leafmc.one/download";
    public static final String API_URL = "https://api.leafmc.one/v2/projects/leaf/versions/" + AbstractPaperVersionFetcher.BUILD_INFO.minecraftVersionId() + "/builds";
    public static final String USER_AGENT = null;

    public ReactiveVersionFetcher() {
        super(
            DOWNLOAD_PAGE,
            "Winds Studio",
            "Leaf",
            "Winds-Studio",
            "Leaf",
            API_URL,
            USER_AGENT,
            ApiType.BIBLIOTHEK
        );
    }
}
