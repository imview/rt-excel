package com.rt.excel;

public class BaseRowEntity extends Object {
    public BaseRowEntity(){}

    private boolean isSuccess;
    private String message;

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
