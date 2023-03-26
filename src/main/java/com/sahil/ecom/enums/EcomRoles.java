package com.sahil.ecom.enums;

public enum EcomRoles {
    ADMIN("ADMIN","ROLE_ADMIN"),
    SELLER("SELLER","ROLE_SELLER"),
    CUSTOMER("CUSTOMER","ROLE_CUSTOMER");

    public final String label;

    public final String role;

    private EcomRoles(String label,String role) {
        this.label = label;
        this.role = role;
    }
}
