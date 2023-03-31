package ru.skb.lab.intellij.plugins.ui;

import javax.swing.*;

public class SettingsPanel {
    private JPasswordField jasyptPassword;
    private JPanel panel;

    public SettingsPanel(PluginSettings pluginSettings) {
        jasyptPassword.setText(pluginSettings.getJasyptPassword());
    }

    public JPasswordField getJasyptPassword() {
        return jasyptPassword;
    }

    public JPanel getPanel() {
        return panel;
    }
}
