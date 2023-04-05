package org.radonlab.raterm.app;

import com.google.common.collect.Maps;
import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;
import org.radonlab.raterm.pty.PtyProcessTtyConnector;
import org.radonlab.raterm.terminal.TtyConnector;
import org.radonlab.raterm.terminal.ui.JediTermWidget;
import org.radonlab.raterm.terminal.ui.UIUtil;
import org.radonlab.raterm.terminal.ui.settings.DefaultSettingsProvider;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Application {
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
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Basic Terminal Shell Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(createMainPanel());
            frame.pack();
            frame.setVisible(true);
        });
    }
}
