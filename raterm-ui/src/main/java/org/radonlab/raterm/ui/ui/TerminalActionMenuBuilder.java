package org.radonlab.raterm.ui.ui;

import org.jetbrains.annotations.NotNull;

public interface TerminalActionMenuBuilder {
    void addAction(@NotNull TerminalAction action);

    void addSeparator();
}
