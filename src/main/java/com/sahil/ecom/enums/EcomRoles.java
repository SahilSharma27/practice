package com.sahil.ecom.enums;

public enum EcomRoles {
    ADMIN("ADMIN"),
    SELLER("SELLER"),
    CUSTOMER("CUSTOMER");

    public final String label;

    private EcomRoles(String label) {
        this.label = label;
    }
}
