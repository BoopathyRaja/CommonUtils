package com.br.commonutils.helper.imagecache;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

class CustomCircleBitmapDisplayer implements BitmapDisplayer {

    protected final Integer strokeColor;
    protected final float strokeWidth;
    protected final int backgroundColor;

    public CustomCircleBitmapDisplayer() {
        this(null, 0, Color.TRANSPARENT);
    }

    public CustomCircleBitmapDisplayer(Integer strokeColor) {
        this(strokeColor, 0, Color.TRANSPARENT);
    }

    public CustomCircleBitmapDisplayer(Integer strokeColor, float strokeWidth) {
        this(strokeColor, strokeWidth, Color.TRANSPARENT);
    }

    public CustomCircleBitmapDisplayer(Integer strokeColor, float strokeWidth, int backgroundColor) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware))
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");

        imageAware.setImageDrawable(new CircleDrawable(bitmap, strokeColor, strokeWidth, backgroundColor));
    }

    public static class CircleDrawable extends Drawable {

        protected final RectF rectF = new RectF();
        protected final RectF bitmapRectF;
        protected final BitmapShader bitmapShader;
        protected final Paint paint;
        protected final Paint paintStroke;
        protected final float strokeWidth;
        protected final int backgroundColor;
        protected float radius;
        protected Bitmap bitmap;
        protected float strokeRadius;

        public CircleDrawable(Bitmap bitmap, Integer strokeColor, float strokeWidth, int backgroundColor) {
            this.bitmap = bitmap;
            radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2;

            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            bitmapRectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());

            if (strokeColor == null) {
                paintStroke = null;
            } else {
                paintStroke = new Paint();
                paintStroke.setStyle(Paint.Style.STROKE);
                paintStroke.setColor(strokeColor);
                paintStroke.setStrokeWidth(strokeWidth);
                paintStroke.setAntiAlias(true);
            }

            this.strokeWidth = strokeWidth;
            strokeRadius = radius - strokeWidth / 2;

            this.backgroundColor = backgroundColor;

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(backgroundColor);
//            paint.setShader(bitmapShader);
//            paint.setFilterBitmap(true);
//            paint.setDither(true);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);

            rectF.set(0, 0, bounds.width(), bounds.height());
            radius = Math.min(bounds.width(), bounds.height()) / 2;
            strokeRadius = radius - strokeWidth / 2;

            // Resize the original bitmap to fit the new bound
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(bitmapRectF, rectF, Matrix.ScaleToFit.FILL);
            bitmapShader.setLocalMatrix(shaderMatrix);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(radius, radius, radius, paint);

            int cx = (canvas.getWidth() - bitmap.getWidth()) >> 1;
            int cy = (canvas.getHeight() - bitmap.getHeight()) >> 1;
            canvas.drawBitmap(bitmap, cx, cy, paint);

            if (paintStroke != null)
                canvas.drawCircle(radius, radius, strokeRadius, paintStroke);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            paint.setColorFilter(cf);
        }
    }
}