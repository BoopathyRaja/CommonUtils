package com.br.commonutils.base.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.br.commonutils.data.permission.DangerousPermission;
import com.br.commonutils.util.CommonUtil;
import com.br.commonutils.util.Util;
import com.br.commonutils.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PermissionUtil {

    public static final int REQUEST_ID_PERMISSION_MULTIPLE = 100;

    public static void checkPermissionAreAddedToManifest(@NonNull Context context, @NonNull List<DangerousPermission> requestedDangerousPermissions) throws Exception {
        List<DangerousPermission> rawDangerousPermission = new ArrayList<>();

        String[] fromManifest = Util.getAddedPermissionsFromManifest(context);
        for (String rawPermission : fromManifest) {
            if (isValidDangerousPermission(rawPermission)) {
                rawDangerousPermission.add(DangerousPermission.toEnum(rawPermission));
            }
        }

        for (DangerousPermission dangerousPermission : requestedDangerousPermissions) {
            if (!rawDangerousPermission.contains(dangerousPermission))
                throw new Exception(dangerousPermission.toString() + " is not added in manifest");
        }
    }

    public static boolean checkStatus(@NonNull Context context, @NonNull DangerousPermission dangerousPermission) {
        int permissionStatus = ContextCompat.checkSelfPermission(context, dangerousPermission.toString());
        return permissionStatus == PackageManager.PERMISSION_GRANTED ? true : false;
    }

    public static List<DangerousPermission> getGranted(@NonNull Context context, @NonNull List<DangerousPermission> dangerousPermissions) {
        List<DangerousPermission> retVal = new ArrayList<>();

        Map<DangerousPermission, Integer> interResult = fillPermissionStatus(context, dangerousPermissions);
        for (DangerousPermission dangerousPermission : interResult.keySet()) {
            if (interResult.get(dangerousPermission) == PackageManager.PERMISSION_GRANTED)
                retVal.add(dangerousPermission);
        }

        return retVal;
    }

    public static List<DangerousPermission> getDenied(@NonNull Context context, @NonNull List<DangerousPermission> dangerousPermissions) {
        List<DangerousPermission> retVal = new ArrayList<>();

        Map<DangerousPermission, Integer> interResult = fillPermissionStatus(context, dangerousPermissions);
        for (DangerousPermission dangerousPermission : interResult.keySet()) {
            if (interResult.get(dangerousPermission) == PackageManager.PERMISSION_DENIED)
                retVal.add(dangerousPermission);
        }

        return retVal;
    }

    public static void processResult(Context context, String[] permissions, int[] grantResults, Map<DangerousPermission, Integer> requestedPermissions, PermissionHandler permissionHandler, PermissionRationaleHandler permissionRationaleHandler) {
        if (grantResults.length > 0) {
            List<DangerousPermission> granted = new ArrayList<>();
            List<DangerousPermission> denied = new ArrayList<>();

            boolean isAllPermissionsGranted = true;
            List<DangerousPermission> permissionRationale = new ArrayList<>();

            for (int i = 0; i < permissions.length; i++)
                requestedPermissions.put(DangerousPermission.toEnum(permissions[i]), grantResults[i]);

            for (DangerousPermission key : requestedPermissions.keySet()) {
                if (requestedPermissions.get(key) == PackageManager.PERMISSION_GRANTED) {
                    granted.add(key);
                } else {
                    isAllPermissionsGranted = false;
                    denied.add(key);
                }

                boolean shouldShowRequestPermissionRationale = permissionRationaleHandler.showRequestPermissionRationale(key.toString());
                if (shouldShowRequestPermissionRationale)
                    permissionRationale.add(key);
            }

            if (Validator.isValid(permissionHandler)) {
                if (isAllPermissionsGranted) {
                    permissionHandler.result(granted, denied);
                } else {
                    if (!permissionRationale.isEmpty() && permissionHandler.permissionRationale()) {
                        PermissionUtil.showDialog(context, permissionRationaleMessage(permissionRationale), (dialog, which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    permissionHandler.permissionRationaleFor(permissionRationale);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
//                                    permissionHandler.info(SOME_PERMISSION_DENIED);
                                    break;
                            }
                        });
                    } else {
                        permissionHandler.info("Some permissions are denied, Go to app settings and enable it");
                    }
                }
            }
        }
    }

    private static boolean isValidDangerousPermission(String rawPermission) {
        return Validator.isValid(DangerousPermission.toEnum(rawPermission));
    }

    private static Map<DangerousPermission, Integer> fillPermissionStatus(Context context, List<DangerousPermission> dangerousPermissions) {
        Map<DangerousPermission, Integer> retVal = new HashMap<>();

        if (Validator.isValid(dangerousPermissions)) {
            for (int i = 0; i < dangerousPermissions.size(); i++) {
                DangerousPermission dangerousPermission = dangerousPermissions.get(i);

                boolean permissionStatus = checkStatus(context, dangerousPermission);
                retVal.put(dangerousPermission, permissionStatus ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED);
            }
        }

        return retVal;
    }

    private static String permissionRationaleMessage(List<DangerousPermission> permissionRationale) {
        String retVal = "";

        List<String> data = new ArrayList<>();
        for (DangerousPermission dangerousPermission : permissionRationale) {
            data.add(dangerousPermission.stringify());
        }

        if (Validator.isValid(data)) {
            retVal = CommonUtil.toCSV(data);
            retVal += data.size() > 1 ? " permission is " : " permissions are ";
            retVal += "required. Did you like to grant permission now?";
        }

        return retVal;
    }

    private static void showDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .setNegativeButton("Cancel", onClickListener)
                .create()
                .show();
    }
}
