package org.radonlab.raterm.term;

import com.google.common.base.Strings;
import org.radonlab.raterm.conf.Preference;
import org.radonlab.raterm.core.Color;
import org.radonlab.raterm.term.palette.ITermColorPalette;
import org.radonlab.raterm.terminal.TerminalColor;
import org.radonlab.raterm.terminal.TextStyle;
import org.radonlab.raterm.terminal.emulator.ColorPalette;
import org.radonlab.raterm.terminal.ui.settings.DefaultSettingsProvider;

import java.awt.*;

public class TermSettingsProvider extends DefaultSettingsProvider {

    private final ColorPaletteLoader paletteLoader = new ColorPaletteLoader();

    private final Preference.Terminal pref;

    private ColorPalette palette = null;

    private TermSettingsProvider(Preference.Terminal pref) {
        this.pref = pref;
    }

    public static TermSettingsProvider from(Preference.Terminal pref) {
        return new TermSettingsProvider(pref);
    }

    private ColorPalette getColorPalette() {
        if (this.palette == null) {
            this.palette = paletteLoader.loadFromSchema();
        }
        return this.palette;
    }

    @Override
    public Font getTerminalFont() {
        String fontName = pref.getFont();
        return Strings.isNullOrEmpty(fontName) ?
                super.getTerminalFont() :
                new Font(fontName, Font.PLAIN, (int) getTerminalFontSize());
    }

    @Override
    public float getTerminalFontSize() {
        return Math.max(pref.getFontSize(), pref.getMinimalFontSize());
    }

    @Override
    public ColorPalette getTerminalColorPalette() {
        return getColorPalette();
    }

    @Override
    public TextStyle getDefaultStyle() {
        ITermColorPalette cp = (ITermColorPalette) getColorPalette();
        Color fg = cp.getDefaultTextColor();
        Color bg = cp.getDefaultBackgroundColor();
        return new TextStyle(TerminalColor.fromColor(fg), TerminalColor.fromColor(bg));
    }

    @Override
    public TextStyle getSelectionColor() {
        return super.getSelectionColor();
    }

    @Override
    public TextStyle getFoundPatternColor() {
        return super.getFoundPatternColor();
    }

    @Override
    public TextStyle getHyperlinkColor() {
        return super.getHyperlinkColor();
    }
}
