package org.radonlab.raterm.app;

import com.formdev.flatlaf.FlatLaf;
import lombok.extern.slf4j.Slf4j;
import org.radonlab.raterm.conf.Configs;
import org.radonlab.raterm.conf.Preference;
import org.radonlab.raterm.tab.ui.GoldenTabbedPane;
import org.radonlab.raterm.term.TermManager;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class Application implements Runnable {

    private Properties properties;

    private TermManager termManager;

    public static void main(String[] args) {
        Application app = new Application();
        app.loadProperties();
        Configs.init(app.properties);
        Configs.loadPreference();
        app.setupLookAndFeel();
        SwingUtilities.invokeLater(app);
    }

    private void loadProperties() {
        try (InputStream is = getClass().getResourceAsStream("/application.properties")) {
            this.properties = new Properties();
            this.properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupLookAndFeel() {
        try {
            Preference.UI pref = Configs.getPreference().getUi();
            // Register LookAndFeel
            UIManager.installLookAndFeel("Dark", "com.formdev.flatlaf.FlatDarkLaf");
            UIManager.installLookAndFeel("Light", "com.formdev.flatlaf.FlatLightLaf");
            // Custom LookAndFeel
            FlatLaf.registerCustomDefaultsSource("themes");
            // Set LookAndFeel
            String lafClassName = UIManager.getSystemLookAndFeelClassName();
            Optional<UIManager.LookAndFeelInfo> targetLafInfo = Arrays.stream(UIManager.getInstalledLookAndFeels())
                    .filter(laf -> laf.getName().equals(pref.getTheme()))
                    .findFirst();
            if (targetLafInfo.isPresent()) {
                lafClassName = targetLafInfo.get().getClassName();
            }
            UIManager.setLookAndFeel(lafClassName);
        } catch (Exception e) {
            log.error("Fails to setup LookAndFeel", e);
        }
    }

    @Override
    public void run() {
        Preference.Terminal pref = Configs.getPreference().getTerminal();
        this.termManager = new TermManager(pref);
        GoldenTabbedPane mainPane = new GoldenTabbedPane(this.termManager);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPane);
        frame.pack();
        frame.setVisible(true);
    }
}
