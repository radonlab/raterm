package org.radonlab.raterm.ui.ui.input;

import org.jetbrains.annotations.NotNull;
import org.radonlab.raterm.core.input.MouseWheelEvent;

public final class AwtMouseWheelEvent extends MouseWheelEvent {
    private final java.awt.event.MouseWheelEvent myAwtMouseWheelEvent;

    public AwtMouseWheelEvent(@NotNull java.awt.event.MouseWheelEvent awtMouseWheelEvent) {
        super(AwtMouseEvent.createButtonCode(awtMouseWheelEvent), AwtMouseEvent.createButtonCode(awtMouseWheelEvent));
        myAwtMouseWheelEvent = awtMouseWheelEvent;
    }

    @Override
    public String toString() {
        return myAwtMouseWheelEvent.toString();
    }
}
