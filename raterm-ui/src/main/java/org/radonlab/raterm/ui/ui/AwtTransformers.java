package org.radonlab.raterm.ui.ui;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.radonlab.raterm.core.Color;
import org.radonlab.raterm.terminal.TerminalColor;

public final class AwtTransformers {

    @Contract(value = "null -> null; !null -> new", pure = true)
    public static @Nullable java.awt.Color toAwtColor(@Nullable Color color) {
        return color == null ? null : new java.awt.Color(color.getRGB(), true);
    }

    @Contract("null -> null; !null -> new")
    public static @Nullable Color fromAwtColor(@Nullable java.awt.Color color) {
        return color == null ? null : new Color(color.getRGB(), true);
    }

    @Contract("null -> null; !null -> new")
    public static @Nullable TerminalColor fromAwtToTerminalColor(@Nullable java.awt.Color color) {
        return color == null ? null : TerminalColor.fromColor(fromAwtColor(color));
    }
}
