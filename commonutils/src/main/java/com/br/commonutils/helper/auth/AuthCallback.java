package com.br.commonutils.helper.auth;

public interface AuthCallback {

    void authSucceeded();

    void authFailed(String error);

    void authCancelled();
}
