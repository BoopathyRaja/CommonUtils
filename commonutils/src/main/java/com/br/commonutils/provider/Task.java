package com.br.commonutils.provider;

public interface Task<T> {

    void success(T result);

    void failure(String message);
}
