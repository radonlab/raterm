package org.radonlab.raterm.tab.ui;

import com.google.common.collect.ImmutableMap;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Map;

public class GoldenTabbedPaneUI extends BasicTabbedPaneUI {
    private static final int width = 240;
    private static final int radius = 8;
    private static final Color selectedColor = new Color(0xffffff);
    private static final Color hoverColor = new Color(0xe0e0e0);
    private static final Map<RenderingHints.Key, Object> renderingHints = ImmutableMap.of(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
            RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE
    );

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        return width;
    }

    @Override
    protected Insets getTabAreaInsets(int tabPlacement) {
        return new Insets(0, radius, 0, radius);
    }

    @Override
    protected void setRolloverTab(int index) {
        int oldIndex = getRolloverTab();
        super.setRolloverTab(index);
        if (index == oldIndex) {
            return;
        }
        // Trigger repainting explicitly
        repaintTab(oldIndex);
        repaintTab(index);
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        int tabCount = tabPane.getTabCount();
        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();
        Rectangle clipRect = g.getClipBounds();
        // Paint tabRuns of tabs from back to front
        for (int i = runCount - 1; i >= 0; i--) {
            int start = tabRuns[i];
            int next = tabRuns[(i == runCount - 1) ? 0 : i + 1];
            int end = (next != 0 ? next - 1 : tabCount - 1);
            int hoverIndex = getRolloverTab();
            for (int j = start; j <= end; j++) {
                if (j != selectedIndex && j != hoverIndex && rects[j].intersects(clipRect)) {
                    paintTab(g, tabPlacement, rects, j, iconRect, textRect);
                }
            }
            // Paint hovered tab
            if (hoverIndex >= 0 && rects[hoverIndex].intersects(clipRect)) {
                paintTab(g, tabPlacement, rects, hoverIndex, iconRect, textRect);
            }
        }
        // Paint selected tab if its in the front run
        // since it may overlap other tabs
        if (selectedIndex >= 0 && rects[selectedIndex].intersects(clipRect)) {
            paintTab(g, tabPlacement, rects, selectedIndex, iconRect, textRect);
        }
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2 = (Graphics2D) g;
        // Enable anti-aliasing
        g2.setRenderingHints(renderingHints);
        // paint tab background
        Color background = getTabBackground(tabIndex, isSelected);
        Shape path = getTabPath(x, y, w, h);
        g2.setColor(background);
        g2.fill(path);
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        // Paint empty border
    }

    private void repaintTab(int tabIndex) {
        if (tabIndex < 0 || tabIndex >= tabPane.getTabCount()) {
            return;
        }
        Rectangle bounds = getTabBounds(tabPane, tabIndex);
        if (bounds == null) {
            return;
        }
        // Increase the repaint region to include some extra parts
        bounds.x -= radius;
        bounds.width += radius * 2;
        tabPane.repaint(bounds);
    }

    private Color getTabBackground(int tabIndex, boolean isSelected) {
        if (isSelected) {
            return selectedColor;
        }
        if (getRolloverTab() == tabIndex && hoverColor != null) {
            return hoverColor;
        }
        // Invisible
        return tabPane.getBackground();
    }

    private void arcTo(GeneralPath path, float x1, float y1, float x2, float y2, float radius) {
        Point2D p0 = path.getCurrentPoint();
        Arc2D.Float arc = new Arc2D.Float(Arc2D.OPEN);
        arc.setArcByTangent(p0, new Point2D.Float(x1, y1), new Point2D.Float(x2, y2), radius);
        path.append(arc, true);
    }

    private Shape getTabPath(int x, int y, int w, int h) {
        float tabLeft = (float) x;
        float tabTop = (float) y;
        float tabRight = tabLeft + w;
        float tabBottom = tabTop + h;
        float tabRadius = (float) radius;
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
