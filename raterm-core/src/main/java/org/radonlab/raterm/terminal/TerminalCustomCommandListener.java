package org.radonlab.raterm.terminal;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TerminalCustomCommandListener {
    void process(@NotNull List<String> args);
}
