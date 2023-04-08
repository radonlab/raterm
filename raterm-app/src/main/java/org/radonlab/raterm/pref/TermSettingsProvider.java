package org.radonlab.raterm.pref;

import com.google.common.base.Strings;
import org.radonlab.raterm.app.Manifest;
import org.radonlab.raterm.terminal.ui.settings.DefaultSettingsProvider;

import java.awt.*;

public class TermSettingsProvider extends DefaultSettingsProvider {
    private static final int minimalFontSize = Manifest.getInt("terminal.minimalFontSize");

    private final Preference.Terminal pref;

    private TermSettingsProvider(Preference.Terminal pref) {
        this.pref = pref;
    }

    public static TermSettingsProvider from(Preference.Terminal pref) {
        return new TermSettingsProvider(pref);
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
        return Math.max(pref.getFontSize(), minimalFontSize);
    }
}
