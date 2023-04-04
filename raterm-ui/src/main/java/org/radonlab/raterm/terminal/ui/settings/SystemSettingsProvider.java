package org.radonlab.raterm.terminal.ui.settings;

import org.jetbrains.annotations.NotNull;
import org.radonlab.raterm.terminal.ui.TerminalActionPresentation;

public interface SystemSettingsProvider {
    @NotNull
    TerminalActionPresentation getOpenUrlActionPresentation();

    @NotNull
    TerminalActionPresentation getCopyActionPresentation();

    @NotNull
    TerminalActionPresentation getPasteActionPresentation();

    @NotNull
    TerminalActionPresentation getClearBufferActionPresentation();

    @NotNull
    TerminalActionPresentation getPageUpActionPresentation();

    @NotNull
    TerminalActionPresentation getPageDownActionPresentation();

    @NotNull
    TerminalActionPresentation getLineUpActionPresentation();

    @NotNull
    TerminalActionPresentation getLineDownActionPresentation();

    @NotNull
    TerminalActionPresentation getFindActionPresentation();

    @NotNull
    TerminalActionPresentation getSelectAllActionPresentation();
}
