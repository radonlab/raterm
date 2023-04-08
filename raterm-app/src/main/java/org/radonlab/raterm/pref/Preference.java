package org.radonlab.raterm.pref;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import dev.dirs.ProjectDirectories;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@NoArgsConstructor
@Slf4j
public class Preference {
    public static final Preference defaultPreference = new Preference("Dark", "Mono", 14);

    private String theme;
    private String font;
    private int fontSize;

    public Preference(String theme, String font, int fontSize) {
        this.theme = theme;
        this.font = font;
        this.fontSize = fontSize;
    }

    private static void initPreference(Path confFile) throws IOException {
        Files.createDirectories(confFile.getParent());
        Files.createFile(confFile);
    }

    public static @NotNull Preference loadPreference() {
        try {
            String configDir = ProjectDirectories.from("org", "radonlab", "raterm").configDir;
            Path confFile = Paths.get(configDir, "config.toml");
            if (!Files.exists(confFile)) {
                initPreference(confFile);
            }
            TomlMapper mapper = new TomlMapper();
            return mapper.readValue(confFile.toFile(), Preference.class);
        } catch (IOException e) {
            log.warn("Cannot load preference file", e);
            return defaultPreference;
        }
    }
}
