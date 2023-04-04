package org.radonlab.raterm.app;

import org.radonlab.raterm.terminal.ui.JediTermWidget;
import org.radonlab.raterm.terminal.ui.settings.DefaultSettingsProvider;

import javax.swing.*;

public class Application {
    private static JPanel createMainPanel() {
        JediTermWidget widget = new JediTermWidget(80, 24, new DefaultSettingsProvider());
//        widget.start();
        return widget;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Basic Terminal Shell Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(createMainPanel());
            frame.pack();
            frame.setVisible(true);
        });
    }
}
