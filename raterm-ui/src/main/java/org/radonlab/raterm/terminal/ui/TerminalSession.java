package org.radonlab.raterm.terminal.ui;

import org.radonlab.raterm.terminal.Terminal;
import org.radonlab.raterm.terminal.TtyConnector;
import org.radonlab.raterm.terminal.model.TerminalTextBuffer;
import org.radonlab.raterm.terminal.debug.DebugBufferType;

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
