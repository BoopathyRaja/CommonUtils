package com.br.commonutils.helper.rest;

import android.support.annotation.NonNull;

public class HeaderParam extends Param {

    public static HeaderParam from(@NonNull String name, @NonNull String value) {
        HeaderParam retVal = new HeaderParam();
        retVal.setName(name);
        retVal.setValue(value);

        return retVal;
    }
}
