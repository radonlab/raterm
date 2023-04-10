package org.radonlab.raterm.pref;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import dev.dirs.ProjectDirectories;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.radonlab.raterm.app.Manifest;
import org.radonlab.raterm.terminal.ui.UIUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Slf4j
public class Preference implements Mergeable<Preference> {
    public static final Preference defaultPreference;
    private static final String appId = Manifest.get("app.id");

    static {
        try (InputStream is = Preference.class.getResourceAsStream("/config.default.toml")) {
            defaultPreference = new TomlMapper().readValue(is, Preference.class);
            if (defaultPreference.terminal.shell == null) {
                if (UIUtil.isWindows) {
                    defaultPreference.terminal.shell = "cmd.exe";
                } else {
                    defaultPreference.terminal.shell = "/bin/bash --login";
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private UI ui;

    @NotNull
    private Terminal terminal;

    public Preference() {
        this.ui = new UI();
        this.terminal = new Terminal();
    }

    private static void initPreference(Path confFile) throws IOException {
        Files.createDirectories(confFile.getParent());
        Files.createFile(confFile);
    }

    public static @NotNull Preference loadPreference() {
        try {
            String[] appIds = appId.split("\\.");
            String configDir = ProjectDirectories.from(appIds[0], appIds[1], appIds[2]).configDir;
            Path confFile = Paths.get(configDir, "config.toml");
            if (!Files.exists(confFile)) {
                initPreference(confFile);
            }
            TomlMapper mapper = new TomlMapper();
            Preference pref = mapper.readValue(confFile.toFile(), Preference.class);
            pref.merge(defaultPreference);
            return pref;
        } catch (IOException e) {
            log.warn("Cannot load preference file", e);
            return defaultPreference;
        }
    }

    @Override
    public void merge(Preference o) {
        this.ui.merge(o.ui);
        this.terminal.merge(o.terminal);
    }

    @Data
    public static class UI implements Mergeable<UI> {
        /**
         * Light/Dark
         */
        private String theme;

        @Override
        public void merge(UI o) {
            this.theme = this.theme == null ? o.theme : this.theme;
        }
    }

    @Data
    public static class Terminal implements Mergeable<Terminal> {
        /**
         * Font
         */
        private String font;

        /**
         * Font size
         */
        private Integer fontSize;

        /**
         * Shell command
         */
        private String shell;

        @Override
        public void merge(Terminal o) {
            this.font = this.font == null ? o.font : this.font;
            this.fontSize = this.fontSize == null ? o.fontSize : this.fontSize;
            this.shell = this.shell == null ? o.shell : this.shell;
        }
    }
}
