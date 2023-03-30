package ru.skb.lab.intellij.plugins.ui;

import com.intellij.openapi.util.NlsContexts;

import java.util.function.Supplier;

/**
 * newTemplate 29.03.2023
 *
 * @author Сергей
 */
public enum ProjectType {
    SMX_K8S("smx_k8s", () ->"smx_k8s", "smx_k8s", ""),
    SMX_INVEST ("smx-invest", () ->"smx-invest", "smx-invest", "");

    public final String type;
    private final Supplier<@NlsContexts.Label String> presentableNameSupplier;
    public final String value;
    public final String descr;

    ProjectType(String type, Supplier<@NlsContexts.Label String> presentableNameSupplier, String value, String descr) {
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
