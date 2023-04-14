package org.radonlab.raterm.tab.ui;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.geom.GeneralPath;

public class GoldenTabbedPaneUI extends BasicTabbedPaneUI {
    Color getTabBackground(int tabIndex, boolean isSelected) {
        Color tabColor = tabPane.getBackgroundAt(tabIndex);
        Color backColor = tabPane.getBackground();
        return isSelected ? tabColor : backColor;
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2 = (Graphics2D) g;
        // paint tab background
        Color background = getTabBackground(tabIndex, isSelected);
        g2.setColor(background);
        GeneralPath path = new GeneralPath();
        g2.fill(path);
    }
}
