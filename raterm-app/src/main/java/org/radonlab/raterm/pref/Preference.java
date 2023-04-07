package org.radonlab.raterm.pref;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import dev.dirs.ProjectDirectories;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Slf4j
public class Preference {
    public static final Preference defaultPreference = new Preference("Dark", "Mono");

    private String theme;
    private String font;

    public Preference(String theme, String font) {
        this.theme = theme;
        this.font = font;
    }

    public static @NotNull Preference loadPreference() {
        try {
            String configDir = ProjectDirectories.from("org", "radonlab", "raterm").configDir;
            Path confFile = Paths.get(configDir, "config.toml");
            TomlMapper mapper = new TomlMapper();
            return mapper.readValue(confFile.toFile(), Preference.class);
        } catch (IOException e) {
            log.warn("Cannot load preference file", e);
            return defaultPreference;
        }
    }
}
