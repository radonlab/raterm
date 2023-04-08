package org.radonlab.raterm.app;

import com.google.common.collect.Maps;
import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;
import lombok.extern.slf4j.Slf4j;
import org.radonlab.raterm.pref.Preference;
import org.radonlab.raterm.pty.PtyProcessTtyConnector;
import org.radonlab.raterm.terminal.TtyConnector;
import org.radonlab.raterm.terminal.ui.JediTermWidget;
import org.radonlab.raterm.terminal.ui.UIUtil;
import org.radonlab.raterm.terminal.ui.settings.DefaultSettingsProvider;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class Application implements Runnable {

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

    private static JPanel createMainPanel() {
        try {
            JediTermWidget widget = new JediTermWidget(80, 24, new DefaultSettingsProvider());
            widget.setTtyConnector(createTty());
            widget.start();
            return widget;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.preloadResource();
        Preference pref = Preference.loadPreference();
        app.setupContext(pref);
        SwingUtilities.invokeLater(app);
    }

    private void preloadResource() {
        UIManager.installLookAndFeel("Flat Dark", "com.formdev.flatlaf.FlatDarkLaf");
        UIManager.installLookAndFeel("Flat Light", "com.formdev.flatlaf.FlatLightLaf");
    }

    private void setupContext(Preference pref) {
        Preference.UI ui = pref.getUi();
        Preference.Terminal term = pref.getTerminal();
        String lafClassName = UIManager.getSystemLookAndFeelClassName();
        Optional<UIManager.LookAndFeelInfo> targetLafInfo = Arrays.stream(UIManager.getInstalledLookAndFeels())
                .filter(laf -> laf.getName().equals(ui.getTheme())).findFirst();
        if (targetLafInfo.isPresent()) {
            lafClassName = targetLafInfo.get().getClassName();
        }
        try {
            UIManager.setLookAndFeel(lafClassName);
        } catch (Exception e) {
            log.error("Fails to set LookAndFeel", e);
        }
//        FlatLaf.registerCustomDefaultsSource("org.radonlab.raterm.themes");
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Basic Terminal Shell Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(createMainPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
