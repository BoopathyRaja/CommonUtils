package com.br.commonutils;

import android.os.Bundle;

import com.br.commonutils.base.CUBasedActivity;
import com.br.commonutils.base.permission.PermissionHandler;
import com.br.commonutils.data.permission.DangerousPermission;
import com.br.commonutils.helper.toaster.Toaster;
import com.br.commonutils.util.CommonUtil;
import com.br.commonutils.view.textview.TextView;

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
        TextView textView = findViewById(R.id.exampleActivity_textView_custom);
        textView.makeResizable(2);

        Toaster.init(getApplicationContext(), true);
        toaster().toast("Initialized");

        permissionCheck(CommonUtil.asList(DangerousPermission.READ_EXTERNAL_STORAGE, DangerousPermission.ACCESS_FINE_LOCATION, DangerousPermission.RECEIVE_SMS));
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

    private void permissionCheck(List<DangerousPermission> dangerousPermissions) {
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
                permissionCheck(dangerousPermissions);
            }

            @Override
            public void info(String message) {
                toaster().toast(message);
            }
        });
    }
}
