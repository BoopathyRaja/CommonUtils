package com.br.commonutils.helper.auth;

public interface AuthCallback {

    void succeeded();

    void failed(String error);

    void cancelled();
}
