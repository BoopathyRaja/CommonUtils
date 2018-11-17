package com.br.commonutils;

import android.os.Bundle;

import com.br.commonutils.base.CUBasedActivity;
import com.br.commonutils.base.permission.PermissionHandler;
import com.br.commonutils.data.permission.DangerousPermission;
import com.br.commonutils.helper.toaster.Toaster;

import java.util.List;

public class ExampleActivity extends CUBasedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        init();
    }

    @Override
    public void init() {
        Toaster.init(getApplicationContext(), true);
        toaster().toast("Initialized");

        toaster().toast("1");
        toaster().toast("2");
        toaster().toast("3");

//        permission(CommonUtil.asList(DangerousPermission.READ_EXTERNAL_STORAGE, DangerousPermission.ACCESS_FINE_LOCATION, DangerousPermission.RECEIVE_SMS));
    }

    private Toaster toaster() {
        Toaster retVal = null;

        try {
            retVal = getToaster();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    private void permission(List<DangerousPermission> dangerousPermissions) {
        requestPermission(dangerousPermissions, new PermissionHandler() {
            @Override
            public void result(List<DangerousPermission> granted, List<DangerousPermission> denied) {
                toaster().toast(granted.size() + " granted");
                toaster().toast(denied.size() + " denied");
            }

            @Override
            public boolean permissionRationale() {
                return true;
            }

            @Override
            public void permissionRationaleFor(List<DangerousPermission> dangerousPermissions) {
                permission(dangerousPermissions);
            }

            @Override
            public void info(String message) {
                toaster().toast(message);
            }
        });
    }
}
