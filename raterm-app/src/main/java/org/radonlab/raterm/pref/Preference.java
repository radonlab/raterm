package org.radonlab.raterm.pref;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class Preference {

    @NotNull
    private UI ui;

    @NotNull
    private Terminal terminal;

    public Preference() {
        this.ui = new UI();
        this.terminal = new Terminal();
    }

    @Data
    public static class UI {
        /**
         * Light/Dark
         */
        private String theme;
    }

    @Data
    public static class Terminal {
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
    }
}
