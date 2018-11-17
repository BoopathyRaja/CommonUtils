package com.br.commonutils.data.common;

import java.io.Serializable;

public class DimenInfo implements Serializable {

    private int width;
    private int height;

    public static DimenInfo from(int width, int height) {
        DimenInfo retVal = new DimenInfo();
        retVal.width = width;
        retVal.height = height;

        return retVal;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
