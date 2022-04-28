package io.cloudflight.architectureicons;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;

public final class Icon {
    private final URL pngUrl;
    private final PlantUmlSprite plantUmlSprite;
    private final String identifier;
    private final String name;

    public Icon(@Nonnull String identifier, @Nonnull String name, @Nullable String pngUrl, @Nullable PlantUmlSprite plantUmlSprite) {
        this.name = name;
        if (pngUrl == null) {
            this.pngUrl = null;
        } else {
            try {
                this.pngUrl = new URL(pngUrl);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Cannot construct URL", e);
            }
        }
        this.plantUmlSprite = plantUmlSprite;
        this.identifier = identifier;
    }

    @Nonnull
    public String getName() {
        return this.name;
    }

    @Nullable
    public URL getPngUrl() {
        return pngUrl;
    }

    @Nullable
    public String getPngAsBase64() {
        return Base64Loader.load(identifier);
    }

    @Nullable
    public String getPngAsBase64ForHtml() {
        String base64 = getPngAsBase64();
        if (base64 != null) {
            return BASE64_IMG_HTML_PREFIX + base64;
        } else {
            return null;
        }
    }

    @Nullable
    public PlantUmlSprite getPlantUmlSprite() {
        return plantUmlSprite;
    }

    private static final String BASE64_IMG_HTML_PREFIX = "data:image/png;base64,";
}


