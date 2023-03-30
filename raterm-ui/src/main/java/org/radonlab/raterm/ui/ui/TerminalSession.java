package org.radonlab.raterm.ui.ui;

import com.jediterm.terminal.Terminal;
import com.jediterm.terminal.TtyConnector;
import org.radonlab.raterm.ui.debug.DebugBufferType;
import com.jediterm.terminal.model.TerminalTextBuffer;

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
