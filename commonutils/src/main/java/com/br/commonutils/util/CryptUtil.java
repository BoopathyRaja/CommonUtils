package com.br.commonutils.util;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class CryptUtil {

    public static String encode(@NonNull String data) throws Exception {
        String retVal;

        try {
            byte[] byteData = data.getBytes("UTF-8");
            retVal = Base64.encodeToString(byteData, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw e;
        }

        return retVal;
    }

    public static String decode(@NonNull String data) throws Exception {
        String retVal;

        try {
            byte[] byteData = Base64.decode(data, Base64.DEFAULT);
            retVal = new String(byteData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw e;
        }

        return retVal;
    }

    public static String encryptWithSHA256(@NonNull String data) throws Exception {
        String retVal;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteData = digest.digest(data.getBytes());

            StringBuffer sha256hex = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                String hexStr = Integer.toHexString(0xff & byteData[i]);

                if (hexStr.length() == 1)
                    sha256hex.append('0');

                sha256hex.append(hexStr);
            }

            retVal = sha256hex.toString();
        } catch (Exception e) {
            throw e;
        }

        return retVal;
    }
}
