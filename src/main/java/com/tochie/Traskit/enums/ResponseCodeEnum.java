package com.tochie.Traskit.enums;

public enum ResponseCodeEnum {

    SUCCESS(0, "Success"),
    ERROR(-1, "Error"),
    FAILED_AUTHENTICATION(-2, "Failed authentication"),
    USER_NOT_FOUND(-3, "User not found"),
    NO_RECORDS_FOUND(-4, "Your search was successful, but no records found"),
    INVALID_REQUEST(-5, "Invalid request "),
    DUPLICATE_EMAIL(20, "Email or username already exists"),
    SERVICE_NOT_AVAILABLE(6, "Service currently unavailable, please try again later"),
    SERVICE_FAILURE(7, "Request failed please contact support"),


            ;

    ResponseCodeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    private final int code;
    private String description;

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
