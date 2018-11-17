package com.br.commonutils.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.commonutils.R;

import java.lang.reflect.Field;

public class ColorUtil {

    @ColorInt
    public static int getColor(@NonNull Context context, @ColorRes int color) {
        if (VersionUtil.isMarshmallow())
            return ContextCompat.getColor(context, color);
        else
            return context.getResources().getColor(color);
    }

    private static int getColorFromAttr(@NonNull Context context, @AttrRes final int attributeColor) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeColor, typedValue, true);

        return typedValue.data;
    }

    public static void changeCursorColor(@NonNull View view) {
        try {
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            field.set(view, R.drawable.background_cursor);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {

        }
    }

    public static void changeImageColor(@NonNull final ImageView imageView, @ColorInt int fromColor, @ColorInt int toColor) {
        ValueAnimator imageColorChangeAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        imageColorChangeAnimation.addUpdateListener(animator -> imageView.setColorFilter((Integer) animator.getAnimatedValue()));
        imageColorChangeAnimation.setDuration(150);
        imageColorChangeAnimation.start();
    }

    public static Bitmap changeImageColor(@NonNull Bitmap bitmap, @ColorInt int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1);

        Paint paint = new Paint();
        paint.setColorFilter(new LightingColorFilter(color, 1));

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);

        return resultBitmap;
    }
}
