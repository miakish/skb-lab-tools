package ru.skb.lab.intellij.plugins.ui;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "PluginSettings",
        storages = {
                @Storage("PluginSettings.xml")}
)
public class PluginSettings implements PersistentStateComponent<PluginSettings> {

    private String jasyptPassword;

    @Override
    public @Nullable PluginSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PluginSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public String getJasyptPassword() {
        return jasyptPassword;
    }

    public void setJasyptPassword(String jasyptPassword) {
        this.jasyptPassword = jasyptPassword;
    }
}
