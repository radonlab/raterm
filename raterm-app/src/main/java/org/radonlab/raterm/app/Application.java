package org.radonlab.raterm.app;

import com.formdev.flatlaf.FlatLaf;
import com.google.common.collect.Maps;
import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;
import lombok.extern.slf4j.Slf4j;
import org.radonlab.raterm.pref.Preference;
import org.radonlab.raterm.pref.TermSettingsProvider;
import org.radonlab.raterm.pty.PtyProcessTtyConnector;
import org.radonlab.raterm.terminal.TtyConnector;
import org.radonlab.raterm.terminal.ui.JediTermWidget;
import org.radonlab.raterm.terminal.ui.UIUtil;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class Application implements Runnable {

    private Preference preference;

    private static TtyConnector createTty() throws IOException {
        Map<String, String> envVars = Maps.newHashMap(System.getenv());
        String[] command;
        if (UIUtil.isWindows) {
            command = new String[]{"cmd.exe"};
        } else {
            command = new String[]{"/bin/bash", "--login"};
            envVars.put("TERM", "xterm-256color");
        }
        PtyProcess process = new PtyProcessBuilder().setCommand(command).setEnvironment(envVars).start();
        return new PtyProcessTtyConnector(process, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.preloadResource();
        app.preference = Preference.loadPreference();
        app.initShell();
        SwingUtilities.invokeLater(app);
    }

    private JPanel createMainPanel() {
        try {
            Preference.Terminal term = this.preference.getTerminal();
            JediTermWidget widget = new JediTermWidget(90, 25, TermSettingsProvider.from(term));
            widget.setTtyConnector(createTty());
            widget.start();
            return widget;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
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
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(createMainPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
