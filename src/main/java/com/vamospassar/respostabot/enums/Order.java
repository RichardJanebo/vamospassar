package com.vamospassar.respostabot.enums;

public enum Order {
    ORDER_APPROVED("order_approved");


    private final String value ;

    Order(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
