package com.br.commonutils.base.permission;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.br.commonutils.data.permission.DangerousPermission;
import com.br.commonutils.validator.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionActivity extends AppCompatActivity implements PermissionRationaleHandler {

    private Map<DangerousPermission, Integer> requestedPermissions = new HashMap<>();
    private PermissionHandler permissionHandler;

    public void requestPermission(@NonNull List<DangerousPermission> dangerousPermissions, @NonNull PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;

        try {
            PermissionUtil.checkPermissionAreAddedToManifest(this, dangerousPermissions);

            List<DangerousPermission> deniedDangerousPermission = PermissionUtil.getDenied(this, dangerousPermissions);
            if (Validator.isValid(deniedDangerousPermission)) {
                String[] requestPermission = new String[dangerousPermissions.size()];
                for (int i = 0; i < dangerousPermissions.size(); i++) {
                    String data = dangerousPermissions.get(i).toString();
                    requestPermission[i] = data;
                }

                ActivityCompat.requestPermissions(this, requestPermission, PermissionUtil.REQUEST_ID_PERMISSION_MULTIPLE);
            } else {
                if (Validator.isValid(permissionHandler))
                    permissionHandler.result(PermissionUtil.getGranted(this, dangerousPermissions), deniedDangerousPermission);
            }
        } catch (Exception e) {
            permissionHandler.info(e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_ID_PERMISSION_MULTIPLE:
                PermissionUtil.processResult(this, permissions, grantResults, requestedPermissions, permissionHandler, this);
        }
    }

    @Override
    public boolean showRequestPermissionRationale(String key) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, key);
    }
}
