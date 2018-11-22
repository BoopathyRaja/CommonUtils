package com.br.commonutils.provider;

public interface Task<T> {

    void taskSucceeded(T result);

    void taskFailed(String message);
}
