package org.radonlab.raterm.tab.ui;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class GoldenTabbedPaneUI extends BasicTabbedPaneUI {
    private static final int tabWidth = 240;
    private static final float tabRadius = 8f;

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        return tabWidth;
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2 = (Graphics2D) g;
        // paint tab background
        Color background = getTabBackground(tabIndex, isSelected);
        Shape path = getTabPath(x, y, w, h);
        g2.setColor(background);
        g2.fill(path);
    }

    private Color getTabBackground(int tabIndex, boolean isSelected) {
        Color tabColor = Color.WHITE;
        Color backColor = tabPane.getBackground();
        return isSelected ? tabColor : backColor;
    }

    private void arcTo(GeneralPath path, float x1, float y1, float x2, float y2, float radius) {
        Point2D p0 = path.getCurrentPoint();
        Arc2D.Float arc = new Arc2D.Float(Arc2D.OPEN);
        arc.setArcByTangent(p0, new Point2D.Float(x1, y1), new Point2D.Float(x2, y2), radius);
        path.append(arc, true);
    }

    private Shape getTabPath(int x, int y, int w, int h) {
        float tabLeft = x;
        float tabTop = y;
        float tabRight = tabLeft + w;
        float tabBottom = tabTop + h;
        GeneralPath path = new GeneralPath();
        // Start from bottom-left
        path.moveTo(tabLeft - tabRadius, tabBottom);
        // Draw the bottom-left corner
        arcTo(path, tabLeft, tabBottom, tabLeft, tabBottom - tabRadius, tabRadius);
        // Draw the ascender and top-left curve
        path.lineTo(tabLeft, tabTop + tabRadius);
        arcTo(path, tabLeft, tabTop, tabLeft + tabRadius, tabTop, tabRadius);
        // Draw the top crossbar and top-right curve
        path.lineTo(tabRight - tabRadius, tabTop);
        arcTo(path, tabRight, tabTop, tabRight, tabTop + tabRadius, tabRadius);
        // Draw the descender and bottom-right corner
        path.lineTo(tabRight, tabBottom - tabRadius);
        arcTo(path, tabRight, tabBottom, tabRight + tabRadius, tabBottom, tabRadius);
        path.closePath();
        return path;
    }
}
