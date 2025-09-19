package ru.skb.lab.intellij.plugins.ui;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.regex.Pattern;

/**
 * newTemplate 31.03.2023
 *
 * @author Сергей
 */
public class PluginSettingsConfigurable implements SearchableConfigurable {
    private PluginSettings pluginSettings;
    private SettingsPanel settingsPanel;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "SKB LAB Tools";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        pluginSettings = PluginSettingsService.getSettings();
        if (null == settingsPanel) {
            settingsPanel = new SettingsPanel(pluginSettings);
        }
        return settingsPanel.getPanel();
    }

    @Override
    public boolean isModified() {
        String password = String.valueOf(settingsPanel.getJasyptPassword().getPassword());
        boolean isCyrillic = Pattern.matches(".*\\p{InCyrillic}.*", password);
        if(!isCyrillic) {
            return (!password.equals(pluginSettings.getJasyptPassword()));
        } else {
            Messages.showMessageDialog("You cannot use Cyrillic in your password!", "Jasypt Password", Messages.getErrorIcon());
            return false;
        }
    }

    @Override
    public void apply() throws ConfigurationException {
        pluginSettings.setJasyptPassword(String.valueOf(settingsPanel.getJasyptPassword().getPassword()));
    }

    @Override
    public @NotNull @NonNls String getId() {
        return "skb-generator";
    }
}
