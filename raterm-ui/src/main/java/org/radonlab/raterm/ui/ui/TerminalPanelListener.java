package org.radonlab.raterm.ui.ui;

import org.radonlab.raterm.terminal.RequestOrigin;
import org.jetbrains.annotations.NotNull;


public interface TerminalPanelListener {
    void onPanelResize(@NotNull RequestOrigin origin);

    void onTitleChanged(String title);
}
