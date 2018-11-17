package com.br.commonutils.helper.toaster;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.br.commonutils.R;
import com.br.commonutils.validator.Validator;

import java.util.ArrayList;

public class Toaster {

    private static Toaster toaster;
    private Context context;
    private ArrayList<Toast> toasts = new ArrayList<>();
    private boolean customView;
    private View view;

    private Toaster(Context context, boolean customView) {
        this.context = context;
        this.customView = customView;
    }

    public static void init(@NonNull Context context, boolean showCustomView) {
        toaster = new Toaster(context, showCustomView);
    }

    public static Toaster getInstance() throws IllegalAccessException {
        if (!Validator.isValid(toaster))
            throw new IllegalAccessException("Call init()");

        return toaster;
    }

    public void setCustomView(boolean customView) {
        this.customView = customView;
    }

    public void setView(@NonNull View view) {
        this.customView = true;
        this.view = view;
    }

    public void toast(@NonNull String message) {
        show(message, ToastPosition.BOTTOM, Toast.LENGTH_SHORT);
    }

    public void toast(@NonNull String message, @NonNull int duration) {
        show(message, ToastPosition.BOTTOM, duration);
    }

    public void toast(@NonNull String message, @NonNull ToastPosition toastPosition) {
        show(message, toastPosition, Toast.LENGTH_SHORT);
    }

    public void toast(@NonNull String message, @NonNull ToastPosition toastPosition, @NonNull int duration) {
        show(message, toastPosition, duration);
    }

    public void clearAllToast() {
        for (Toast toast : toasts) {
            toast.cancel();
        }

        toasts.clear();
    }

    private void show(String message, ToastPosition toastPosition, int duration) {
        if (customView) {
            if (!Validator.isValid(view))
                view = getCustomView(message);

            message = "";
        }

        String finalMessage = message;

        new Handler(Looper.getMainLooper()).post(() -> {
            int gravity = Gravity.BOTTOM;

            switch (toastPosition) {
                case TOP:
                    gravity = Gravity.TOP;
                    break;

                case BOTTOM:
                    gravity = Gravity.BOTTOM;
                    break;

                case CENTER:
                    gravity = Gravity.CENTER;
                    break;
            }

            Toast toast = Toast.makeText(context, finalMessage, duration);
            toast.setGravity(gravity, 0, 100);

            if (Validator.isValid(view))
                toast.setView(view);

            toast.show();

            toasts.add(toast);
        });
    }

    private View getCustomView(@NonNull final String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_toast, null, false);

        TextView textView = view.findViewById(R.id.toast_textView);
        textView.setText(message);

        return view;
    }

    public enum ToastPosition {
        TOP,
        BOTTOM,
        CENTER
    }
}