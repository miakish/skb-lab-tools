package ru.skb.lab.intellij.plugins.ui;

import com.intellij.openapi.util.NlsContexts;

import java.util.function.Supplier;

/**
 * newTemplate 29.03.2023
 *
 * @author Сергей
 */
public enum AppType {
    SERVICE("SERVICE", () ->"Service", "srv", ""),
    ADAPTER ("ADAPTER", () ->"Adapter", "adp", "");

    public final String type;
    private final Supplier<@NlsContexts.Label String> presentableNameSupplier;
    public final String value;
    public final String descr;

    AppType(String type, Supplier<@NlsContexts.Label String> presentableNameSupplier, String value, String descr) {
        this.type = type;
        this.presentableNameSupplier = presentableNameSupplier;
        this.value = value;
        this.descr = descr;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public @NlsContexts.Label String getPresentableName() {
        return presentableNameSupplier.get();
    }

    public String getDescr() {
        return descr;
    }
}
