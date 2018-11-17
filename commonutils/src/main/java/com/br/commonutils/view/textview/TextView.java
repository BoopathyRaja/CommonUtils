package com.br.commonutils.view.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.br.commonutils.R;
import com.br.commonutils.util.VersionUtil;
import com.br.commonutils.validator.Validator;
import com.br.commonutils.view.FontMode;
import com.br.commonutils.view.FontType;
import com.br.commonutils.view.TextUtil;

public class TextView extends AppCompatTextView {

    private static final String READ_MORE = "...Read More";
    private static final String READ_LESS = "...Read Less";
    private static final int DEFAULT_MAX_LINES_AT_SHRINK_STATE = 5;

    private Context context;
    private AttributeSet attributeSet;
    private FontType fontType;
    private FontMode fontMode;
    private int maxLineAtShrinkState = DEFAULT_MAX_LINES_AT_SHRINK_STATE;

    public TextView(Context context) {
        super(context);

        this.context = context;
        init();
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        this.attributeSet = attrs;
        init();
    }

    public void makeResizable(int maxLineAtShrinkState) {
        this.maxLineAtShrinkState = maxLineAtShrinkState;
        makeResizable(this, maxLineAtShrinkState, true);
    }

    public void setFontStyle(FontType fontType, FontMode fontMode) {
        this.fontType = fontType;
        this.fontMode = fontMode;

        TextUtil.applyFont(this, context, fontType, fontMode);
    }

    private void init() {
        getAttributeSet();
        TextUtil.applyFont(this, context, fontType, fontMode);
    }

    private void getAttributeSet() {
        TypedArray typedArray = null;

        try {
            typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TextView);

            fontType = FontType.values()[typedArray.getInt(R.styleable.TextView_fontType, FontType.ROBOTO_REGULAR.ordinal())];
            fontMode = FontMode.values()[typedArray.getInt(R.styleable.TextView_fontMode, FontMode.NORMAL.ordinal())];
        } finally {
            if (Validator.isValid(typedArray))
                typedArray.recycle();
        }
    }

    private void makeResizable(final TextView textView, final int maxLine, final boolean readMore) {
        if (!Validator.isValid(textView.getTag()))
            textView.setTag(textView.getText());

        ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver treeObserver = textView.getViewTreeObserver();
                if (VersionUtil.isJellyBean())
                    treeObserver.removeGlobalOnLayoutListener(this);
                else
                    treeObserver.removeOnGlobalLayoutListener(this);

                if (textView.getLineCount() < maxLine) {
                    String text = textView.getText().toString();

                    textView.setText(text);
                } else if (maxLine > 0 && textView.getLineCount() >= maxLine) {
                    // Compressed data
                    int lineEndIndex = textView.getLayout().getLineEnd(maxLine - 1);
                    String text = textView.getText().subSequence(0, lineEndIndex - READ_MORE.length() + 1) + " " + READ_MORE;

                    textView.setText(text);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    textView.setText(addClickablePartResizable(textView.getText().toString(), textView, READ_MORE, readMore), BufferType.SPANNABLE);
                } else {
                    // Expanded data
                    int lineEndIndex = textView.getLayout().getLineEnd(textView.getLayout().getLineCount() - 1);
                    String text = textView.getText().subSequence(0, lineEndIndex) + " " + READ_LESS;

                    textView.setText(text);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    textView.setText(addClickablePartResizable(textView.getText().toString(), textView, READ_LESS, readMore), BufferType.SPANNABLE);
                }
            }
        });
    }

    private SpannableStringBuilder addClickablePartResizable(final String spannedText, final TextView textView, final String spannableText, final boolean readMore) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spannedText);

        if (spannedText.contains(spannableText)) {
            spannableStringBuilder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    textView.setLayoutParams(textView.getLayoutParams());
                    textView.setText(textView.getTag().toString(), BufferType.SPANNABLE);
                    textView.invalidate();

                    if (readMore)
                        makeResizable(textView, -1, false); // Expanded data
                    else
                        makeResizable(textView, maxLineAtShrinkState, true); // Compressed data
                }
            }, spannedText.indexOf(spannableText), spannedText.indexOf(spannableText) + spannableText.length(), 0);
        }

        return spannableStringBuilder;
    }
}
