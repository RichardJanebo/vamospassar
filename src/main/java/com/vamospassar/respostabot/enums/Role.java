package com.vamospassar.respostabot.enums;

public enum Role {
    ADMIN("admin"),
    CUSTOMER("customer");

    private final  String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
