package org.radonlab.raterm.tab.ui;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class GoldenTabPane extends JTabbedPane {

    @Setter
    private TabCallback callback;

    public GoldenTabPane(@NotNull TabCallback callback) {
        this.callback = callback;
        this.setUI(new TabPaneUI());
        this.addTab("Tab1", this.callback.createTab());
    }

    private static class TabPaneUI extends BasicTabbedPaneUI {
        @Override
        protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects,
                                int tabIndex, Rectangle iconRect, Rectangle textRect) {
            super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
            Rectangle tabRect = rects[tabIndex];
            g.setColor(Color.MAGENTA);
            g.drawRect(tabRect.x, tabRect.y, tabRect.width, tabRect.height);
        }
    }
}
