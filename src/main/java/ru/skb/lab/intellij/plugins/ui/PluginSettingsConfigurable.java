package ru.skb.lab.intellij.plugins.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * newTemplate 31.03.2023
 *
 * @author Сергей
 */
public class PluginSettingsConfigurable implements Configurable {
    private final PluginSettings pluginSettings;
    private SettingsPanel settingsPanel;

    public PluginSettingsConfigurable(Project project) {
        this.pluginSettings = project.getService(PluginSettings.class);
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "SKB LAB Tools";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (null == settingsPanel) {
            settingsPanel = new SettingsPanel(pluginSettings);
        }
        return settingsPanel.getPanel();
    }

    @Override
    public boolean isModified() {
        String password = String.valueOf(settingsPanel.getJasyptPassword().getPassword());
        return (!password.equals(pluginSettings.getJasyptPassword()));
    }

    @Override
    public void apply() throws ConfigurationException {
        pluginSettings.setJasyptPassword(String.valueOf(settingsPanel.getJasyptPassword().getPassword()));
    }
}
