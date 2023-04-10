package org.radonlab.raterm.app;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Manifest {
    private static final Properties props;

    static {
        try (InputStream is = Manifest.class.getResourceAsStream("/manifest.properties")) {
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Manifest() {
    }

    public static @NotNull String get(@NotNull String key) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new NullPointerException(String.format("Property of \"%s\" is null", key));
        }
        return value;
    }

    public static int getInt(@NotNull String key) {
        return Integer.parseInt(get(key));
    }

    public static float getFloat(@NotNull String key) {
        return Float.parseFloat(get(key));
    }
}
