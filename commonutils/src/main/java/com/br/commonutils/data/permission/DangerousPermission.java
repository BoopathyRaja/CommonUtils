package com.br.commonutils.data.permission;

import android.Manifest;

import com.br.commonutils.provider.Stringifier;

import java.util.HashMap;
import java.util.Map;

public enum DangerousPermission implements Stringifier {

    READ_CALENDAR(Manifest.permission.READ_CALENDAR),
    WRITE_CALENDAR(Manifest.permission.WRITE_CALENDAR),

    READ_CALL_LOG(Manifest.permission.READ_CALL_LOG),
    WRITE_CALL_LOG(Manifest.permission.WRITE_CALL_LOG),
    PROCESS_OUTGOING_CALLS(Manifest.permission.PROCESS_OUTGOING_CALLS),

    CAMERA(Manifest.permission.CAMERA),

    READ_CONTACTS(Manifest.permission.READ_CONTACTS),
    WRITE_CONTACTS(Manifest.permission.WRITE_CONTACTS),
    GET_ACCOUNTS(Manifest.permission.GET_ACCOUNTS),

    ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    ACCESS_COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),

    MICRO_PHONE(Manifest.permission.RECORD_AUDIO),

    READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE),
    READ_PHONE_NUMBERS(Manifest.permission.READ_PHONE_NUMBERS),
    CALL_PHONE(Manifest.permission.CALL_PHONE),
    ANSWER_PHONE_CALLS(Manifest.permission.ANSWER_PHONE_CALLS),
    ADD_VOICE_MAIL(Manifest.permission.ADD_VOICEMAIL),
    USE_SIP(Manifest.permission.USE_SIP),

    SENSORS(Manifest.permission.BODY_SENSORS),

    SEND_SMS(Manifest.permission.SEND_SMS),
    RECEIVE_SMS(Manifest.permission.RECEIVE_SMS),
    READ_SMS(Manifest.permission.READ_SMS),
    RECEIVE_WAP_PUSH(Manifest.permission.RECEIVE_WAP_PUSH),
    RECEIVE_MMS(Manifest.permission.RECEIVE_MMS),

    READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE),
    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    private static final Map<String, DangerousPermission> VALUES = new HashMap<>();

    static {
        for (DangerousPermission type : values())
            VALUES.put(type.value, type);
    }

    private String value;

    DangerousPermission(String value) {
        this.value = value;
    }

    public static DangerousPermission toEnum(String permissionValue) {
        DangerousPermission retVal = null;

        DangerousPermission result = VALUES.get(permissionValue);
        if (result != null)
            retVal = result;

        return retVal;
    }

    @Override
    public String stringify() {
        String retVal = "";

        switch (this) {
            case READ_CALENDAR:
            case WRITE_CALENDAR:
                retVal = "Calendar";
                break;

            case CAMERA:
                retVal = "Camera";
                break;

            case READ_CONTACTS:
            case WRITE_CONTACTS:
                retVal = "Contacts";
                break;

            case GET_ACCOUNTS:
                retVal = "Account";
                break;

            case ACCESS_FINE_LOCATION:
            case ACCESS_COARSE_LOCATION:
                retVal = "Location";
                break;

            case MICRO_PHONE:
            case READ_PHONE_STATE:
                retVal = "Phone";
                break;

            case CALL_PHONE:
            case READ_CALL_LOG:
            case WRITE_CALL_LOG:
            case ADD_VOICE_MAIL:
            case USE_SIP:
            case PROCESS_OUTGOING_CALLS:
                retVal = "Call";
                break;

            case SENSORS:
                retVal = "Sensor";
                break;

            case SEND_SMS:
            case RECEIVE_SMS:
            case READ_SMS:
                retVal = "SMS";
                break;

            case RECEIVE_WAP_PUSH:
                retVal = "WAP";
                break;

            case RECEIVE_MMS:
                retVal = "MMS";
                break;

            case READ_EXTERNAL_STORAGE:
            case WRITE_EXTERNAL_STORAGE:
                retVal = "Storage";
                break;
        }

        return retVal;
    }

    @Override
    public String toString() {
        return value;
    }
}
