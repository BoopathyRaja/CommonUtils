package com.br.commonutils.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.br.commonutils.R;
import com.br.commonutils.validator.Validator;

public class DrawableUtil {

    private static Drawable defaultCircularUser;

    public static Drawable getApplicationIcon(@NonNull Context context) {
        try {
            return context.getPackageManager().getApplicationIcon(context.getPackageName());
        } catch (Exception e) {
            return null;
        }
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int resource) {
        if (VersionUtil.isMarshmallow())
            return ContextCompat.getDrawable(context, resource);
        else
            return context.getResources().getDrawable(resource);
    }

    public static Bitmap getBitmap(@NonNull Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (Validator.isValid(bitmapDrawable.getBitmap()))
                return bitmapDrawable.getBitmap();
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        else
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap getCircularBitmap(@NonNull Bitmap bitmap) {
        Bitmap retVal = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff424242);

        Canvas canvas = new Canvas(retVal);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return retVal;
    }

    public static Drawable getDefaultCircularUser(Context context, int size) {
        if (!Validator.isValid(defaultCircularUser)) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable(context, R.drawable.ic_img_user);
            Drawable resizeDrawable = resizeDrawable(context, bitmapDrawable, DeviceUtil.dpToPx(size), DeviceUtil.dpToPx(size));
            Bitmap circularBitmap = getCircularBitmap(((BitmapDrawable) resizeDrawable).getBitmap());

            defaultCircularUser = new BitmapDrawable(context.getResources(), circularBitmap);
        }

        return defaultCircularUser;
    }

    public static Drawable resizeDrawable(@NonNull Context context, @NonNull Drawable drawable, int width, int height) {
        if ((Validator.isValid(drawable)) && !(drawable instanceof BitmapDrawable))
            return drawable;

        Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), width, height, false);
        drawable = new BitmapDrawable(context.getResources(), bitmap);

        return drawable;
    }
}
