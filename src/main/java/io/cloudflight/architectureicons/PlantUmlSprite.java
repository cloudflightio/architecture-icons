package io.cloudflight.architectureicons;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class PlantUmlSprite {
    private final String name;
    private final List<URL> includes;

    public PlantUmlSprite(@Nonnull String name, String... includes) {
        this.name = name;
        this.includes = Collections.unmodifiableList(Arrays.stream(includes).map(url -> {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Cannot construct URL", e);
            }
        }).collect(Collectors.toList()));
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public List<URL> getIncludes() {
        return includes;
    }
}
