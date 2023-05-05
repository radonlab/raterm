package org.radonlab.raterm.app;

import com.formdev.flatlaf.FlatLaf;
import lombok.extern.slf4j.Slf4j;
import org.radonlab.raterm.conf.Configs;
import org.radonlab.raterm.conf.Preference;
import org.radonlab.raterm.tab.ui.GoldenTabbedPane;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class Application implements Runnable {

    private Properties properties;
    private TtyManager ttyManager;

    public static void main(String[] args) {
        Application app = new Application();
        app.loadProperties();
        app.preloadResources();
        Configs.loadPreference(app.properties);
        app.initShell();
        SwingUtilities.invokeLater(app);
    }

    private void loadProperties() {
        try {
            this.properties = new Properties();
            this.properties.load(getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void preloadResources() {
        UIManager.installLookAndFeel("Dark", "com.formdev.flatlaf.FlatDarkLaf");
        UIManager.installLookAndFeel("Light", "com.formdev.flatlaf.FlatLightLaf");
    }

    private void initShell() {
        try {
            Preference.UI ui = Configs.preference.getUi();
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
        Preference.Terminal term = Configs.preference.getTerminal();
        this.ttyManager = new TtyManager(term);
        GoldenTabbedPane mainPane = new GoldenTabbedPane(this.ttyManager);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPane);
        frame.pack();
        frame.setVisible(true);
    }
}
