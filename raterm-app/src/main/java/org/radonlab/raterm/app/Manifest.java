package org.radonlab.raterm.app;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Manifest {
    private static final Manifest instance = new Manifest();

    private final Properties props;

    private Manifest() {
        try (InputStream is = Manifest.class.getResourceAsStream("/manifest.properties")) {
            this.props = new Properties();
            this.props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull String get(@NotNull String key) {
        String value = instance.props.getProperty(key);
        if (value == null) {
            throw new RuntimeException(String.format("Property %s not exist", key));
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
