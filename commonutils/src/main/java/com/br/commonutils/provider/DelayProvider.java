package com.br.commonutils.provider;

public interface DelayProvider {

    void completed();

    default long delayFor() {
        return 1500;    // milliSeconds
    }
}
