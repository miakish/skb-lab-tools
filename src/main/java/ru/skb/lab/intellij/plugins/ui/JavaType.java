package ru.skb.lab.intellij.plugins.ui;

import com.intellij.openapi.util.NlsContexts;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * newTemplate 28.03.2023
 *
 * @author Сергей
 */
public enum JavaType {

    JAVA_11  ("JAVA_11", () ->"Java 11", "11", ""),
    JAVA_8("JAVA_8", () ->"Java 8", "8", ""),
    JAVA_CRYPTO_PRO ("JAVA_CRYPTO_PRO", () ->"java CryptoPro", "CryptoPro", "");

    public final String type;
    private final Supplier<@NlsContexts.Label String> presentableNameSupplier;
    public final String value;
    public final String descr;

    JavaType(String type, Supplier<@NlsContexts.Label String> presentableNameSupplier, String value, String descr) {
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

    public static JavaType fromValue(String value) {
        return Arrays.stream(values()).filter(v->v.value.equals(value)).findAny().get();
    }

}
