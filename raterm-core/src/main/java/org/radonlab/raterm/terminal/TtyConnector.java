package org.radonlab.raterm.terminal;

import org.jetbrains.annotations.NotNull;
import org.radonlab.raterm.core.util.TermSize;

import java.io.IOException;

public interface TtyConnector {
    int read(char[] buf, int offset, int length) throws IOException;

    void write(byte[] bytes) throws IOException;

    void write(String string) throws IOException;

    boolean isConnected();

    default void resize(@NotNull TermSize termSize) {
        // support old implementations not overriding this method
        resize(new java.awt.Dimension(termSize.getColumns(), termSize.getRows()));
    }

    int waitFor() throws InterruptedException;

    boolean ready() throws IOException;

    String getName();

    void close();

    @Deprecated
    default void resize(@NotNull java.awt.Dimension termWinSize) {
        // support old implementations overriding neither `resize(Dimension)` nor this method
        resize(termWinSize, new java.awt.Dimension(0, 0));
    }

    @Deprecated
    default void resize(java.awt.Dimension termWinSize, java.awt.Dimension pixelSize) {
        throw new IllegalStateException("This method shouldn't be called. " +
                getClass() + " should override TtyConnector.resize(com.jediterm.core.util.TermSize)");
    }

    @Deprecated
    default boolean init(Questioner q) {
        return true;
    }
}
