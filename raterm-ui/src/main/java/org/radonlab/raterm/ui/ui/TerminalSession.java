package org.radonlab.raterm.ui.ui;

import org.radonlab.raterm.terminal.Terminal;
import org.radonlab.raterm.terminal.TtyConnector;
import org.radonlab.raterm.ui.debug.DebugBufferType;
import org.radonlab.raterm.terminal.model.TerminalTextBuffer;

/**
 * @author traff
 */
public interface TerminalSession {
    void start();

    String getBufferText(DebugBufferType type, int stateIndex);

    TerminalTextBuffer getTerminalTextBuffer();

    Terminal getTerminal();

    TtyConnector getTtyConnector();

    void close();
}
