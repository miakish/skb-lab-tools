package ru.skb.lab.intellij.plugins.ui;

import com.intellij.openapi.util.NlsContexts;

import java.util.function.Supplier;

public enum ProfileType {
    DEFAULT  ("DEFAULT", () ->"DEFAULT", "DEFAULT", "REPLICAS_COUNT: 1\n" +
            "maxUnavailable: 0\n" +
            "maxSurge: 1\n" +
            "cpuReq: 100m\n" +
            "memReq: 128Mi\n" +
            "cpuLim: 200m\n" +
            "memLim: 300Mi"),
    HUGE  ("HUGE", () ->"HUGE", "HUGE", "REPLICAS_COUNT: 1\n" +
            "maxUnavailable: 0\n" +
            "maxSurge: 1\n" +
            "cpuReq: 100m\n" +
            "memReq: 664Mi\n" +
            "cpuLim: 200m\n" +
            "memLim: 1328Mi"),
    HUGE2("HUGE2", () ->"HUGE2", "HUGE2", "REPLICAS_COUNT: 2\n" +
            "maxUnavailable: 1\n" +
            "maxSurge: 1\n" +
            "cpuReq: 100m\n" +
            "memReq: 664Mi\n" +
            "cpuLim: 200m\n" +
            "memLim: 1328Mi"),
    TINY ("TINY", () ->"TINY", "TINY", "REPLICAS_COUNT: 1\n" +
            "maxUnavailable: 0\n" +
            "maxSurge: 1\n" +
            "cpuReq: 100m\n" +
            "memReq: 256Mi\n" +
            "cpuLim: 200m\n" +
            "memLim: 512Mi");

    public final String type;
    private final Supplier<@NlsContexts.Label String> presentableNameSupplier;
    public final String value;
    public final String descr;

    ProfileType(String type, Supplier<@NlsContexts.Label String> presentableNameSupplier, String value, String descr) {
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
