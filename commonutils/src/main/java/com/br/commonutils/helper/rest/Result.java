package com.br.commonutils.helper.rest;

import com.google.gson.annotations.Expose;

public class Result<T> {

    @Expose
    private boolean status;

    @Expose
    private T data;

    @Expose
    private String message;

    @Expose
    private double timeTaken;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(double timeTaken) {
        this.timeTaken = timeTaken;
    }
}