package com.br.commonutils.base.permission;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.br.commonutils.data.permission.DangerousPermission;
import com.br.commonutils.validator.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionFragment extends Fragment implements PermissionRationaleHandler {

    private Map<DangerousPermission, Integer> requestedPermissions = new HashMap<>();
    private PermissionHandler permissionHandler;

    public void requestPermission(@NonNull List<DangerousPermission> dangerousPermissions, @NonNull PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;

        try {
            PermissionUtil.checkPermissionAreAddedToManifest(getContext(), dangerousPermissions);

            List<DangerousPermission> deniedDangerousPermission = PermissionUtil.getDenied(getActivity(), dangerousPermissions);
            if (Validator.isValid(deniedDangerousPermission)) {
                String[] requestPermission = new String[dangerousPermissions.size()];
                for (int i = 0; i < deniedDangerousPermission.size(); i++) {
                    String data = deniedDangerousPermission.get(i).toString();
                    requestPermission[i] = data;
                }

                this.requestPermissions(requestPermission, PermissionUtil.REQUEST_ID_PERMISSION_MULTIPLE);
            } else {
                if (Validator.isValid(permissionHandler))
                    permissionHandler.result(PermissionUtil.getGranted(getContext(), dangerousPermissions), deniedDangerousPermission);
            }
        } catch (Exception e) {
            permissionHandler.info(e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_ID_PERMISSION_MULTIPLE:
                PermissionUtil.processResult(getContext(), permissions, grantResults, requestedPermissions, permissionHandler, this);
        }
    }

    @Override
    public boolean showRequestPermissionRationale(String key) {
        return shouldShowRequestPermissionRationale(key);
    }
}
