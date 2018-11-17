package com.br.commonutils.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.br.commonutils.R;
import com.br.commonutils.validator.Validator;
import com.br.commonutils.view.FontMode;
import com.br.commonutils.view.FontType;

public class TextViewInfo extends LinearLayout {

    private Context context;
    private AttributeSet attributeSet;

    private float valueSize;
    @ColorInt
    private int valueColor;
    private String value;

    private float hintSize;
    @ColorInt
    private int hintColor;
    private String hint;

    private FontType fontType;
    private FontMode fontMode;

    private TextView textViewHint;
    private TextView textViewValue;

    public TextViewInfo(Context context) {
        super(context);

        this.context = context;
        init();
    }

    public TextViewInfo(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    public TextViewInfo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    public void setValueSize(float valueSize) {
        this.valueSize = valueSize;

        textViewValue.setTextSize(valueSize);
    }

    public void setValueColor(@ColorInt int valueColor) {
        this.valueColor = valueColor;

        textViewValue.setTextColor(valueColor);
    }

    public void setHintSize(float hintSize) {
        this.hintSize = hintSize;

        textViewHint.setTextSize(hintSize);
    }

    public void setHintColor(@ColorInt int hintColor) {
        this.hintColor = hintColor;

        textViewHint.setTextColor(hintColor);
    }

    public void setFontStyle(FontType fontType, FontMode fontMode) {
        this.fontType = fontType;
        this.fontMode = fontMode;

        textViewValue.setFontStyle(fontType, fontMode);
        textViewHint.setFontStyle(fontType, fontMode);
    }

    public void setText(String value, String hint) {
        this.hint = hint;
        this.value = value;

        textViewValue.setText(value);
        textViewValue.setVisibility(Validator.isValid(value) ? VISIBLE : GONE);

        textViewHint.setText(hint);
        textViewHint.setVisibility(Validator.isValid(hint) ? VISIBLE : GONE);
    }

    public void setSpannableText(Spannable value, Spannable hint) {
        textViewValue.setText(Validator.isValid(value) ? value : "", android.widget.TextView.BufferType.SPANNABLE);
        textViewValue.setVisibility(Validator.isValid(value) ? VISIBLE : GONE);

        textViewHint.setText(Validator.isValid(hint) ? hint : "", android.widget.TextView.BufferType.SPANNABLE);
        textViewHint.setVisibility(Validator.isValid(hint) ? VISIBLE : GONE);
    }

    private void init() {
        getAttributeSet();

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_textview_info, this, true);

        textViewValue = view.findViewById(R.id.textViewInfo_textView_value);
        setValueSize(valueSize);
        setValueColor(valueColor);

        textViewHint = view.findViewById(R.id.textViewInfo_textView_hint);
        setHintSize(hintSize);
        setHintColor(hintColor);

        setFontStyle(fontType, fontMode);

        setText(value, hint);
    }

    private void getAttributeSet() {
        TypedArray typedArray = null;

        try {
            typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TextViewInfo);

            valueSize = typedArray.getFloat(R.styleable.TextViewInfo_valueSize, 14f);
            valueColor = typedArray.getColor(R.styleable.TextViewInfo_valueColor, context.getResources().getColor(R.color.lightTextSecondary));
            value = typedArray.getString(R.styleable.TextViewInfo_value);

            hintSize = typedArray.getFloat(R.styleable.TextViewInfo_hintSize, 12f);
            hintColor = typedArray.getColor(R.styleable.TextViewInfo_hintColor, context.getResources().getColor(R.color.lightTextHint));
            hint = typedArray.getString(R.styleable.TextViewInfo_hint);

            fontType = FontType.values()[typedArray.getInt(R.styleable.TextViewInfo_fontType, FontType.ROBOTO_LIGHT.ordinal())];
            fontMode = FontMode.values()[typedArray.getInt(R.styleable.TextViewInfo_fontMode, FontMode.ITALIC.ordinal())];
        } finally {
            if (Validator.isValid(typedArray))
                typedArray.recycle();
        }
    }
}
