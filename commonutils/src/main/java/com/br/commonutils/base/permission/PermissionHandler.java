package com.br.commonutils.base.permission;

import com.br.commonutils.data.permission.DangerousPermission;

import java.util.List;

public interface PermissionHandler {

    void result(List<DangerousPermission> granted, List<DangerousPermission> denied);

    boolean permissionRationale();

    void permissionRationaleFor(List<DangerousPermission> dangerousPermissions);

    void info(String message);
}
