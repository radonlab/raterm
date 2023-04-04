package org.radonlab.raterm.terminal.ui;

import org.jetbrains.annotations.NotNull;
import org.radonlab.raterm.terminal.RequestOrigin;


public interface TerminalPanelListener {
    void onPanelResize(@NotNull RequestOrigin origin);

    void onTitleChanged(String title);
}
