package org.radonlab.raterm.tab.ui;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GoldenTabPane extends JTabbedPane {

    @Setter
    private TabCallback callback;

    public GoldenTabPane(@NotNull TabCallback callback) {
        this.callback = callback;
        this.addTab("Tab1", this.callback.createTab());
    }
}
