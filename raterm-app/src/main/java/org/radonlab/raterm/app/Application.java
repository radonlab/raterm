package org.radonlab.raterm.app;

import com.formdev.flatlaf.FlatLaf;
import lombok.extern.slf4j.Slf4j;
import org.radonlab.raterm.pref.Preference;
import org.radonlab.raterm.tab.ui.GoldenTabbedPane;

import javax.swing.*;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class Application implements Runnable {

    private Preference preference;
    private TtyManager ttyManager;

    public static void main(String[] args) {
        Application app = new Application();
        app.preloadResource();
        app.preference = Preference.loadPreference();
        app.initShell();
        SwingUtilities.invokeLater(app);
    }

    private void preloadResource() {
        UIManager.installLookAndFeel("Dark", "com.formdev.flatlaf.FlatDarkLaf");
        UIManager.installLookAndFeel("Light", "com.formdev.flatlaf.FlatLightLaf");
    }

    private void initShell() {
        try {
            Preference.UI ui = this.preference.getUi();
            // Custom LookAndFeel
            FlatLaf.registerCustomDefaultsSource("themes");
            // Set LookAndFeel
            String lafClassName = UIManager.getSystemLookAndFeelClassName();
            Optional<UIManager.LookAndFeelInfo> targetLafInfo = Arrays.stream(UIManager.getInstalledLookAndFeels())
                    .filter(laf -> laf.getName().equals(ui.getTheme())).findFirst();
            if (targetLafInfo.isPresent()) {
                lafClassName = targetLafInfo.get().getClassName();
            }
            UIManager.setLookAndFeel(lafClassName);
        } catch (Exception e) {
            log.error("Fails to setup shell", e);
        }
    }

    @Override
    public void run() {
        this.ttyManager = new TtyManager(this.preference.getTerminal());
        GoldenTabbedPane mainPane = new GoldenTabbedPane(this.ttyManager);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPane);
        frame.pack();
        frame.setVisible(true);
    }
}
