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

public class Toaster {

    private Context context;
    private String message;
    private Duration duration = Duration.LONG;
    private Align align = Align.BOTTOM;
    private boolean customView = false;
    private View view;

    private Toaster(Context context) {
        this.context = context;
    }

    public static Toaster with(@NonNull Context context) {
        return new Toaster(context);
    }

    public Toaster message(@NonNull String message) {
        this.message = message;
        return this;
    }

    public Toaster duration(@NonNull Duration duration) {
        this.duration = duration;
        return this;
    }

    public Toaster align(@NonNull Align align) {
        this.align = align;
        return this;
    }

    public Toaster customView(boolean customView) {
        this.customView = customView;
        return this;
    }

    public Toaster view(@NonNull View view) {
        this.view = view;
        this.customView = true;

        return this;
    }

    public void show() {
        makeToaster(message, duration, align);
    }

    private void makeToaster(String message, Duration duration, Align align) {
        if (customView) {
            if (!Validator.isValid(view))
                view = getDefaultCustomView(message);

            message = "";
        }

        String finalMessage = message;

        new Handler(Looper.getMainLooper()).post(() -> {
            int durationTime = Toast.LENGTH_LONG;
            switch (duration) {
                case SHORT:
                    durationTime = Toast.LENGTH_SHORT;
                    break;

                case LONG:
                    durationTime = Toast.LENGTH_LONG;
                    break;
            }

            int gravity = Gravity.BOTTOM;
            switch (align) {
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

            Toast toast = Toast.makeText(context, finalMessage, durationTime);
            toast.setGravity(gravity, 0, 100);

            if (Validator.isValid(view))
                toast.setView(view);

            toast.show();
        });
    }

    private View getDefaultCustomView(String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_toast, null, false);

        TextView textView = view.findViewById(R.id.toast_textView);
        textView.setText(message);

        return view;
    }

    public enum Align {
        TOP,
        BOTTOM,
        CENTER
    }

    public enum Duration {
        SHORT,
        LONG
    }
}