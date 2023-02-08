package com.sahil.ecom.enums;

public enum TokenType {
    REFRESH("REFRESH"),
    ACCESS("ACCESS");

    public final String label;

    private TokenType(String label) {
        this.label = label;
    }
}
