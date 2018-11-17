package com.br.commonutils.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ImageUtil {

    public static File compressImage(@NonNull File file, float compressImageWidth, float compressImageHeight) throws Exception {
        if (!file.exists())
            throw new FileNotFoundException("File does not exists");

        BitmapFactory.Options options = new BitmapFactory.Options();
        // By setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If you try the use the bitmap here, you will getByJobCode null.
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        int actualImageWidth = options.outWidth;
        int actualImageHeight = options.outHeight;

        float actualImageRatio = actualImageWidth / actualImageHeight;
        float compressImageRatio = compressImageWidth / compressImageHeight;

        // Width and Height values are set maintaining the aspect ratio of the image
        if (actualImageHeight > compressImageHeight || actualImageWidth > compressImageWidth) {
            if (actualImageRatio < compressImageRatio) {
                actualImageRatio = compressImageHeight / actualImageHeight;
                actualImageWidth = (int) (actualImageRatio * actualImageWidth);
                actualImageHeight = (int) compressImageHeight;
            } else if (actualImageRatio > compressImageRatio) {
                actualImageRatio = compressImageWidth / actualImageWidth;
                actualImageHeight = (int) (actualImageRatio * actualImageHeight);
                actualImageWidth = (int) compressImageWidth;
            } else {
                actualImageHeight = (int) compressImageHeight;
                actualImageWidth = (int) compressImageWidth;
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualImageWidth, actualImageHeight); // Setting inSampleSize value allows to load a scaled down version of the original image
        options.inJustDecodeBounds = false; // inJustDecodeBounds set to false to load the actual bitmap
        options.inPurgeable = true; // This options allow android to claim the bitmap memory if it runs low on memory
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        Bitmap scaledBitmap = Bitmap.createBitmap(actualImageWidth, actualImageHeight, Bitmap.Config.ARGB_8888);

        float ratioX = actualImageWidth / (float) options.outWidth;
        float ratioY = actualImageHeight / (float) options.outHeight;
        float middleX = actualImageWidth / 2.0f;
        float middleY = actualImageHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        Matrix matrix = new Matrix();
        ExifInterface exif = new ExifInterface(file.getAbsolutePath());
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        if (orientation == 6)
            matrix.postRotate(90);
        else if (orientation == 3)
            matrix.postRotate(180);
        else if (orientation == 8)
            matrix.postRotate(270);

        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        File compressImageFile = file;
        if (!compressImageFile.exists())
            compressImageFile.mkdirs();

        FileOutputStream fileOutputStream = new FileOutputStream(compressImageFile);
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);

        return compressImageFile;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int requiredImageWidth, int requiredImageHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requiredImageHeight || width > requiredImageWidth) {
            final int heightRatio = Math.round((float) height / (float) requiredImageHeight);
            final int widthRatio = Math.round((float) width / (float) requiredImageWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = requiredImageWidth * requiredImageHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
