package org.radonlab.raterm.terminal.ui.settings;

import org.jetbrains.annotations.NotNull;
import org.radonlab.raterm.terminal.HyperlinkStyle;
import org.radonlab.raterm.terminal.TextStyle;
import org.radonlab.raterm.terminal.emulator.ColorPalette;
import org.radonlab.raterm.terminal.model.TerminalTypeAheadSettings;

import java.awt.*;

public interface UserSettingsProvider {
    ColorPalette getTerminalColorPalette();

    Font getTerminalFont();

    float getTerminalFontSize();

    /**
     * @return vertical scaling factor
     */
    default float getLineSpacing() {
        return 1.0f;
    }

    default boolean shouldDisableLineSpacingForAlternateScreenBuffer() {
        return false;
    }

    default boolean shouldFillCharacterBackgroundIncludingLineSpacing() {
        return true;
    }

    TextStyle getDefaultStyle();

    TextStyle getSelectionColor();

    TextStyle getFoundPatternColor();

    TextStyle getHyperlinkColor();

    HyperlinkStyle.HighlightMode getHyperlinkHighlightingMode();

    boolean useInverseSelectionColor();

    boolean copyOnSelect();

    boolean pasteOnMiddleMouseClick();

    boolean emulateX11CopyPaste();

    boolean useAntialiasing();

    int maxRefreshRate();

    boolean audibleBell();

    boolean enableMouseReporting();

    int caretBlinkingMs();

    boolean scrollToBottomOnTyping();

    boolean DECCompatibilityMode();

    boolean forceActionOnMouseReporting();

    int getBufferMaxLinesCount();

    boolean altSendsEscape();

    boolean ambiguousCharsAreDoubleWidth();

    @NotNull
    TerminalTypeAheadSettings getTypeAheadSettings();

    boolean sendArrowKeysInAlternativeMode();
}
