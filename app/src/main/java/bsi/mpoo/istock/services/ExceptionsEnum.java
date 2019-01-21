package bsi.mpoo.istock.services;

public enum ExceptionsEnum {
    EMAIL_ALREADY_REGISTERED(1), CLIENT_ALREADY_REGISTERED(2), CLIENT_NOT_REGISTERED(3),
    PRODUCT_ALREADY_REGISTERED(4), PRODUCT_NOT_REGISTERED(5), USER_NOT_REGISTERED(6),
    ORDER_NOT_REGISTERED(7);
    private int value;

    ExceptionsEnum(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
