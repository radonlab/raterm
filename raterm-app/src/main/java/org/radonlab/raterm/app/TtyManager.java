package org.radonlab.raterm.app;

import com.google.common.collect.Maps;
import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.radonlab.raterm.pref.Preference;
import org.radonlab.raterm.pref.TermSettingsProvider;
import org.radonlab.raterm.pty.PtyProcessTtyConnector;
import org.radonlab.raterm.tab.ui.TabCallback;
import org.radonlab.raterm.terminal.TtyConnector;
import org.radonlab.raterm.terminal.ui.JediTermWidget;
import org.radonlab.raterm.terminal.ui.UIUtil;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class TtyManager implements TabCallback {

    private final Preference.Terminal pref;

    public TtyManager(Preference.Terminal pref) {
        this.pref = pref;
    }

    private @Nullable TtyConnector createTty() {
        try {
            Map<String, String> envVars = Maps.newHashMap(System.getenv());
            String[] command = this.pref.getShell().split(" ");
            if (!UIUtil.isWindows) {
                envVars.put("TERM", "xterm-256color");
            }
            PtyProcess process = new PtyProcessBuilder().setCommand(command).setEnvironment(envVars).start();
            return new PtyProcessTtyConnector(process, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Fails to start PTY process", e);
            return null;
        }
    }

    @Override
    public JComponent createTab() {
        JediTermWidget widget = new JediTermWidget(90, 25, TermSettingsProvider.from(this.pref));
        TtyConnector ttyConnector = createTty();
        if (ttyConnector == null) {
            return null;
        }
        widget.setTtyConnector(ttyConnector);
        widget.start();
        return widget;
    }

    @Override
    public void destroyTab(JComponent tab) {
        if (tab instanceof JediTermWidget) {
            JediTermWidget widget = (JediTermWidget) tab;
            widget.stop();
        }
    }
}
