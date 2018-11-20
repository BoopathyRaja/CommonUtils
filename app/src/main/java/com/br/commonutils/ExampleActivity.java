package com.br.commonutils;

import android.os.Bundle;

import com.br.commonutils.base.CUBasedActivity;
import com.br.commonutils.base.permission.PermissionHandler;
import com.br.commonutils.data.common.DimenInfo;
import com.br.commonutils.data.permission.DangerousPermission;
import com.br.commonutils.helper.preference.Preference;
import com.br.commonutils.helper.rest.HeaderParam;
import com.br.commonutils.helper.rest.MethodType;
import com.br.commonutils.helper.rest.QueryParam;
import com.br.commonutils.helper.rest.RestBuilder;
import com.br.commonutils.helper.snacker.Snacker;
import com.br.commonutils.helper.snacker.SnackerHandler;
import com.br.commonutils.helper.toaster.Toaster;
import com.br.commonutils.provider.Task;
import com.br.commonutils.util.CommonUtil;
import com.br.commonutils.view.textview.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleActivity extends CUBasedActivity implements SnackerHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        init();
    }

    @Override
    public void init() {
        /*********************************************** TEXTVIEW RESIZEABLE ***********************************************/
        TextView textView = findViewById(R.id.exampleActivity_textView_custom);
        textView.makeResizable(2);


        /*********************************************** PREFERENCE ***********************************************/
        String preferenceName = "SAMPLE";

        // Save & Retrieve - String
        Preference.make(this, preferenceName)
                .save("STRING", "Data");
        String string = Preference.make(this, preferenceName)
                .retrieve("STRING");


        // Save & Retrieve - Boolean
        Preference.make(this, preferenceName)
                .save("BOOLEAN", true);
        Boolean status = Preference.make(this, preferenceName)
                .retrieve("BOOLEAN", Boolean.class);


        // Save & Retrieve - Object
        Preference.make(this, preferenceName)
                .save("OBJECT", DimenInfo.from(10, 20));
        DimenInfo dimenInfo = Preference.make(this, preferenceName)
                .retrieve("OBJECT", DimenInfo.class);


        // Save & Retrieve - List
        List<DimenInfo> dimenInfos = CommonUtil.asList(DimenInfo.from(10, 20), DimenInfo.from(30, 40));
        Preference.make(this, preferenceName)
                .save("LIST", dimenInfos);
        List<DimenInfo> listData = Preference.make(this, preferenceName)
                .retrieveAsList("LIST", DimenInfo.class);


        // Save & Retrieve - Map
        Map<String, DimenInfo> map = new HashMap<>();
        map.put("1", DimenInfo.from(1, 1));
        map.put("2", DimenInfo.from(2, 2));
        Preference.make(this, preferenceName)
                .save("MAP", map);
        Map<String, DimenInfo> mapData = Preference.make(this, preferenceName)
                .retrieveAsMap("MAP", String.class, DimenInfo.class);


        /*********************************************** TOASTER ***********************************************/
        Toaster.with(this)
                .message("Toaster")
                .align(Toaster.Align.CENTER)
                .customView(true)
                .show();


        /*********************************************** SNACKER ***********************************************/
        Snacker.with(this)
                .message("Make permission request")
                .actionText("OK", this)
                .duration(Snacker.Duration.LONG)
//                .view(findViewById(R.id.exampleActivity_coordinatorLayout)) // Optional
                .show();


        /*********************************************** REST CALL MAKER ***********************************************/
        RestBuilder.make("BASE_URI")
                .path("PATH")
                .queryParam(QueryParam.from("", ""))
                .headerParam(HeaderParam.from("", ""))
                .methodType(MethodType.POST)
                .payload(null)
                .responseType(SampleResponse.class)
                .execute(new Task<SampleResponse>() {
                    @Override
                    public void success(SampleResponse result) {

                    }

                    @Override
                    public void failure(String message) {

                    }
                });
    }

    @Override
    public void onActionClicked() {
        /*********************************************** PERMISSION ***********************************************/
        permissionCheck(
                CommonUtil.asList(
                        DangerousPermission.READ_EXTERNAL_STORAGE,
                        DangerousPermission.ACCESS_FINE_LOCATION,
                        DangerousPermission.RECEIVE_SMS)
        );
    }

    // Make Permission
    private void permissionCheck(List<DangerousPermission> dangerousPermissions) {
        requestPermission(dangerousPermissions, new PermissionHandler() {
            @Override
            public void result(List<DangerousPermission> granted, List<DangerousPermission> denied) {
                Toaster.with(getApplicationContext())
                        .message("Granted " + granted.size() + "\nDenied " + denied.size())
                        .show();
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
                Toaster.with(getApplicationContext())
                        .message(message)
                        .show();
            }
        });
    }
}
