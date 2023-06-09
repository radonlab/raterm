package org.radonlab.raterm.pty;

import com.pty4j.PtyProcess;
import com.pty4j.WinSize;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.radonlab.raterm.core.util.TermSize;
import org.radonlab.raterm.terminal.ProcessTtyConnector;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author traff
 */
public class PtyProcessTtyConnector extends ProcessTtyConnector {

    public PtyProcessTtyConnector(@NotNull PtyProcess process, @NotNull Charset charset) {
        this(process, charset, null);
    }

    public PtyProcessTtyConnector(@NotNull PtyProcess process, @NotNull Charset charset, @Nullable List<String> commandLine) {
        super(process, charset, commandLine);
    }

    @Override
    public void resize(@NotNull TermSize termSize) {
        if (isConnected()) {
            PtyProcess ptyProcess = (PtyProcess) getProcess();
            ptyProcess.setWinSize(new WinSize(termSize.getColumns(), termSize.getRows()));
        }
    }

    @Override
    public String getName() {
        return "Local";
    }
}
