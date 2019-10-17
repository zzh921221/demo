package com.my.segmentation.dto;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


public enum ResultStatus {
    OK(200, "OK"),
    NO_AUTH(401, "NO_AUTH"),
    ERROR(500, "ERROR");

    private Integer code;
    private String message;

    private ResultStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
