package org.radonlab.raterm.tab.ui;

import com.formdev.flatlaf.ui.FlatTabbedPaneUI;
import com.formdev.flatlaf.ui.FlatUIUtils;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

public class GoldenTabbedPane extends JTabbedPane {

    @Setter
    private TabCallback callback;

    public GoldenTabbedPane(@NotNull TabCallback callback) {
        this.callback = callback;
        this.setUI(new TabbedPaneUI());
        this.addTab("Tab1", this.callback.createTab());
    }

    private static class TabbedPaneUI extends FlatTabbedPaneUI {
        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                          boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g;
            // paint tab background
            Color background = getTabBackground(tabPlacement, tabIndex, isSelected);
            g2.setColor(FlatUIUtils.deriveColor(background, tabPane.getBackground()));
            GeneralPath path = new GeneralPath();
            g2.fill(path);
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                      boolean isSelected) {
        }

        @Override
        protected void paintTabSelection(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h) {
        }
    }
}
