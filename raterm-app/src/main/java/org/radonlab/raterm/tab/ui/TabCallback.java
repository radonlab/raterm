package org.radonlab.raterm.tab.ui;

import javax.swing.*;

public interface TabCallback {
    JComponent createTab();
    void destroyTab(JComponent tab);
}
