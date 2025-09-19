package ru.skb.lab.intellij.plugins.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "PluginSettings",
        storages = {
                @Storage("PluginSettings.xml")}
)
public class PluginSettingsService implements PersistentStateComponent<PluginSettings> {
    private PluginSettings state = new PluginSettings();

    @Override
    public @Nullable PluginSettings getState() {
        return state;
    }

    public static PluginSettings getSettings() {
        PluginSettingsService service = getService();
        return service.getState();
    }

    public static PluginSettingsService getService() {
        return ApplicationManager.getApplication().getService(PluginSettingsService.class);
    }

    @Override
    public void loadState(@NotNull PluginSettings state) {
        this.state = state;
    }
}
