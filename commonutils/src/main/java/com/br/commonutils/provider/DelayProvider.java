package com.br.commonutils.provider;

public interface DelayProvider {

    void delayCompleted();

    default long delayFor() {
        return 1500;    // milliSeconds
    }
}
