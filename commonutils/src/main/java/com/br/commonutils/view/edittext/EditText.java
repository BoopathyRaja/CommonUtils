package com.br.commonutils.view.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.br.commonutils.R;
import com.br.commonutils.util.DeviceUtil;
import com.br.commonutils.util.DrawableUtil;
import com.br.commonutils.validator.Validator;
import com.br.commonutils.view.FontMode;
import com.br.commonutils.view.FontType;
import com.br.commonutils.view.TextUtil;

public class EditText extends AppCompatEditText {

    private Context context;
    private AttributeSet attributeSet;
    private FontType fontType;
    private FontMode fontMode;
    private EditTextType editTextType;
    @DrawableRes
    private int customBackground;
    private Drawable hintIcon;

    public EditText(Context context) {
        super(context);

        this.context = context;
        init();
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    public void setFontType(FontType fontType, FontMode fontMode) {
        this.fontType = fontType;
        this.fontMode = fontMode;

        TextUtil.applyFont(this, context, fontType, fontMode);
    }

    public void setEditTextType(EditTextType editTextType) {
        this.editTextType = editTextType;

        int background = 0;
        switch (editTextType) {
            case BOX:
                background = R.drawable.background_edittext;
                break;

            case STROKE:
                background = R.drawable.background_edittext_stroke;
                break;

            case LINE:
                background = R.drawable.background_edittext_line;
                break;

            case CUSTOM:
                background = customBackground;
                break;
        }

        setBackground(DrawableUtil.getDrawable(context, background));
    }

    public void setHintIcon(Drawable hintIcon) {
        this.hintIcon = hintIcon;

        if (Validator.isValid(hintIcon)) {
            setCompoundDrawablesWithIntrinsicBounds(hintIcon, null, null, null);
            setCompoundDrawablePadding(30);
        }

        int padding = DeviceUtil.dpToPx(5);
        setPadding(padding, padding, padding, padding);
    }

    private void init() {
        getAttributeSet();
        setHeight(DeviceUtil.dpToPx(48));
        TextUtil.applyFont(this, context, fontType, fontMode);
        setEditTextType(editTextType);
        setHintIcon(hintIcon);
    }

    private void getAttributeSet() {
        TypedArray typedArray = null;

        try {
            typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.EditText);

            fontType = FontType.values()[typedArray.getInt(R.styleable.EditText_fontType, FontType.ROBOTO_REGULAR.ordinal())];
            fontMode = FontMode.values()[typedArray.getInt(R.styleable.EditText_fontMode, FontMode.NORMAL.ordinal())];
            editTextType = EditTextType.values()[typedArray.getInt(R.styleable.EditText_type, EditTextType.BOX.ordinal())];
            customBackground = typedArray.getResourceId(R.styleable.EditText_customBackground, R.drawable.background_edittext);
            hintIcon = typedArray.getDrawable(R.styleable.EditText_hintIcon);
        } finally {
            if (Validator.isValid(typedArray))
                typedArray.recycle();
        }
    }
}
