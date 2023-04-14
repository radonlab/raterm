package org.radonlab.raterm.tab.ui;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GoldenTabbedPane extends JTabbedPane {

    @Setter
    private TabCallback callback;

    public GoldenTabbedPane(@NotNull TabCallback callback) {
        this.callback = callback;
        this.setUI(new GoldenTabbedPaneUI());
        this.addTab("Tab1", this.callback.createTab());
        this.addTab("Tab2", this.callback.createTab());
        this.addTab("Tab3", this.callback.createTab());
    }
}
