package com.sahil.Ecom.enums;

public enum EcomRoles {
    ADMIN("ADMIN"),
    SELLER("SELLER"),
    ROLE_CUSTOMER("CUSTOMER");

    public final String label;

    private EcomRoles(String label) {
        this.label = label;
    }
}
