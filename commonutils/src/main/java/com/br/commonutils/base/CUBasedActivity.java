package com.br.commonutils.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.br.commonutils.R;
import com.br.commonutils.base.permission.PermissionActivity;
import com.br.commonutils.helper.jsonizer.GsonJsonizer;
import com.br.commonutils.helper.preference.Preference;
import com.br.commonutils.helper.toaster.Toaster;
import com.br.commonutils.validator.Validator;

public abstract class CUBasedActivity extends PermissionActivity {

    public final String EXTRA_OVERRIDE_TRANSITION = "extraOverrideTransition";
    private ProgressDialog progressDialog;

    public abstract void init();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        progressDialog = new ProgressDialog(CUBasedActivity.this, R.style.ProgressBar);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        processBundleData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        dismissProgressBar();

        try {
            getToaster().clearAllToast();
        } catch (IllegalAccessException e) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showProgressBar() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
            progressDialog.setContentView(R.layout.view_progressbar);
        }
    }

    public void dismissProgressBar() {
        progressDialog.dismiss();
    }

    public CUBasedFragment getFragment(@NonNull Class<CUBasedFragment> fragment) {
        CUBasedFragment retVal = null;

        try {
            String packageName = fragment.getPackage().getName();
            String className = packageName + "." + fragment.getSimpleName();

            retVal = (CUBasedFragment) Class.forName(className).newInstance();
        } catch (Exception e) {
            // Never happen
        }

        return retVal;
    }

    public void updateFragmentView(@IdRes int containerViewId, @NonNull Fragment fragment, boolean addToBackStack) {
        try {
            String tag = fragment.getClass().getSimpleName();
            boolean popBackStackImmediate = getSupportFragmentManager().popBackStackImmediate(tag, 0);
            if (!popBackStackImmediate) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(containerViewId, fragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(addToBackStack ? tag : null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (Exception e) {

        }
    }

    public GsonJsonizer getGsonJsonizer() {
        return GsonJsonizer.getInstance();
    }

    public Preference getPreferenceFactory() throws IllegalAccessException {
        return Preference.getInstance();
    }

    public Toaster getToaster() throws IllegalAccessException {
        Toaster toastFactory = Toaster.getInstance();
        toastFactory.setCustomView(false);

        return toastFactory;
    }

    private void processBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (Validator.isValid(bundle)) {
            boolean overrideTransition = bundle.getBoolean(EXTRA_OVERRIDE_TRANSITION, true);
            if (overrideTransition)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}