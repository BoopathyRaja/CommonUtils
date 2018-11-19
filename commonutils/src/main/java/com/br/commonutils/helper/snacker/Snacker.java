package com.br.commonutils.helper.snacker;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.br.commonutils.R;
import com.br.commonutils.util.ColorUtil;
import com.br.commonutils.validator.Validator;

public class Snacker {

    private final int DEFAULT_BACKGROUND_COLOR = R.color.black;
    private final int DEFAULT_TEXT_COLOR = R.color.white;
    private final int DEFAULT_ACTION_TEXT_COLOR = R.color.redPrimary;
    private Context context;
    private String message;
    private String actionText;
    private SnackerHandler snackerHandler;
    private Duration duration;
    private View view;

    private Snacker(Context context) {
        this.context = context;
    }

    public static Snacker with(@NonNull Context context) {
        return new Snacker(context);
    }

    public Snacker message(@NonNull String message) {
        this.message = message;
        return this;
    }

    public Snacker actionText(@NonNull String actionText) {
        this.actionText = actionText;
        return this;
    }

    public Snacker actionText(@NonNull String actionText, @NonNull SnackerHandler snackerHandler) {
        this.actionText = actionText;
        this.snackerHandler = snackerHandler;
        return this;
    }

    public Snacker duration(@NonNull Duration duration) {
        this.duration = duration;
        return this;
    }

    public Snacker view(@NonNull View view) {
        this.view = view;
        return this;
    }

    public void show() {
        if (!Validator.isValid(view))
            view = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);

        makeSnacker(view, message, actionText, duration, snackerHandler);
    }

    private void makeSnacker(View view, String message, String actionText, Duration duration, SnackerHandler snackerHandler) {
        Snackbar snackbar = getDefaultView(view, message, duration);
        snackbar.setAction(actionText, view1 -> {
            if (snackbar.isShown())
                snackbar.dismiss();

            if (Validator.isValid(snackerHandler))
                snackerHandler.onActionClicked();
        });

        snackbar.show();
    }

    private Snackbar getDefaultView(View view, String message, Duration duration) {
        int durationTime = Snackbar.LENGTH_LONG;
        switch (duration) {
            case SHORT:
                durationTime = Snackbar.LENGTH_SHORT;
                break;

            case LONG:
                durationTime = Snackbar.LENGTH_LONG;
                break;

            case INDEFINITE:
                durationTime = Snackbar.LENGTH_INDEFINITE;
                break;
        }

        Snackbar retVal = Snackbar.make(view, message, durationTime);
        retVal.setActionTextColor(ColorUtil.getColor(context, DEFAULT_ACTION_TEXT_COLOR));

        View _view = retVal.getView();
        _view.setBackgroundColor(ColorUtil.getColor(context, DEFAULT_BACKGROUND_COLOR));

        TextView textView = _view.findViewById(R.id.snackbar_text);
        textView.setTextColor(ColorUtil.getColor(context, DEFAULT_TEXT_COLOR));

        TextView textViewAction = _view.findViewById(R.id.snackbar_action);
        textViewAction.setTextColor(ColorUtil.getColor(context, DEFAULT_ACTION_TEXT_COLOR));

        return retVal;
    }

    public enum Duration {
        SHORT,
        LONG,
        INDEFINITE
    }
}
