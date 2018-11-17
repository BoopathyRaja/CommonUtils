package com.br.commonutils.helper.rest;

import android.support.annotation.NonNull;

public class QueryParam extends Param {

    public static QueryParam from(@NonNull String name, @NonNull String value) {
        QueryParam retVal = new QueryParam();
        retVal.setName(name);
        retVal.setValue(value);

        return retVal;
    }
}
