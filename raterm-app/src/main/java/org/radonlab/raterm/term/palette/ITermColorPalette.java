package org.radonlab.raterm.term.palette;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.radonlab.raterm.core.Color;
import org.radonlab.raterm.terminal.emulator.ColorPalette;

public class ITermColorPalette extends ColorPalette {

    private final Color[] indexColors;

    @Getter
    private final Color defaultTextColor;

    @Getter
    private final Color defaultBackgroundColor;

    public ITermColorPalette(Color[] indexColors, Color defaultTextColor, Color defaultBackgroundColor) {
        this.indexColors = indexColors;
        this.defaultTextColor = defaultTextColor;
        this.defaultBackgroundColor = defaultBackgroundColor;
    }

    @Override
    protected @NotNull Color getForegroundByColorIndex(int colorIndex) {
        return this.indexColors[colorIndex];
    }

    @Override
    protected @NotNull Color getBackgroundByColorIndex(int colorIndex) {
        return this.indexColors[colorIndex];
    }
}
