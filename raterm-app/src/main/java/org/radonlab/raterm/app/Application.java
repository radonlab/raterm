package org.radonlab.raterm.app;

import javax.swing.*;

public class Application {
    private static JPanel createMainPanel() {
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
