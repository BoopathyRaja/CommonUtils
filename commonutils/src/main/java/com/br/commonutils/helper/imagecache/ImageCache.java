package com.br.commonutils.helper.imagecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.widget.ImageView;

import com.br.commonutils.R;
import com.br.commonutils.data.common.DimenInfo;
import com.br.commonutils.util.ColorUtil;
import com.br.commonutils.util.DrawableUtil;
import com.br.commonutils.validator.Validator;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.File;
import java.io.InputStream;

public class ImageCache {

    public static final String IMAGE_DRAWABLE = "drawable://";
    public static final String IMAGE_CONTENT = "content://";
    public static final String IMAGE_ASSETS = "assets://";
    public static final String IMAGE_FILE = "file://";
    public static final String IMAGE_STREAM = "stream://";
    private static ImageCache imageCache;
    private Context context;
    private ImageCacheBase imageCacheBase;

    private ImageCache(@NonNull Context context) {
        this.context = context;
        imageCacheBase = new ImageCacheBase(context);
    }

    public static void init(@NonNull Context context) {
        imageCache = new ImageCache(context);
    }

    public static ImageCache getInstance() {
        if (!Validator.isValid(imageCache))
            throw new RuntimeException("Call init()");

        return imageCache;
    }

    public ImageLoader getImageLoader() {
        return imageCacheBase.getImageLoader();
    }

    public void load(@NonNull ImageView imageView, String path, @DrawableRes int defaultImage) {
        load(imageView, path, defaultImage, null);
    }

    public void load(@NonNull ImageView imageView, String path, @DrawableRes int defaultImage, @NonNull DimenInfo dimenInfo) {
        renderImage(imageView, path, defaultImage, dimenInfo);
    }

    public void load(@NonNull ImageView imageView, InputStream inputStream, @NonNull String key, @DrawableRes int defaultImage, @NonNull DimenInfo dimenInfo) {
        renderImage(imageView, inputStream, key, defaultImage, dimenInfo);
    }

    public void loadRoundedCorner(@NonNull ImageView imageView, String path, @DrawableRes int defaultImage) {
        loadRoundedCorner(imageView, path, defaultImage, null);
    }

    public void loadRoundedCorner(@NonNull ImageView imageView, String path, @DrawableRes int defaultImage, @NonNull DimenInfo dimenInfo) {
        renderRoundedCorner(imageView, path, defaultImage, dimenInfo);
    }

    public void loadCircular(@NonNull ImageView imageView, String path, @DrawableRes int defaultImage) {
        loadCircular(imageView, path, defaultImage, null);
    }

    public void loadCircular(@NonNull ImageView imageView, String path, @DrawableRes int defaultImage, DimenInfo dimenInfo) {
        renderCircular(imageView, path, defaultImage, dimenInfo);
    }

    public void loadCircular(@NonNull ImageView imageView, String path, @DrawableRes int defaultImage, @ColorRes int strokeColor) {
        loadCircular(imageView, path, defaultImage, strokeColor, null);
    }

    public void loadCircular(@NonNull ImageView imageView, String path, @DrawableRes int defaultImage, @ColorRes int strokeColor, DimenInfo dimenInfo) {
        renderCircular(imageView, path, defaultImage, strokeColor, dimenInfo);
    }

    public void refresh() {
        imageCacheBase.refreshImageLoader(context);
    }

    public void clear(@NonNull ImageView... imageViews) {
        for (ImageView imageView : imageViews) {
//            clearCache((String) imageView.getTag());
            imageView.setTag(null);
        }
    }

    public void clearCache(@NonNull String imagePath) {
        clearDiscCache(imagePath);
        clearMemoryCache(imagePath);
    }

    public void clearDiscCache() {
        imageCacheBase.getImageLoader().clearDiskCache();
    }

    public void clearDiscCache(@NonNull String imagePath) {
        DiskCache diskCache = imageCacheBase.getImageLoader().getDiskCache();
        File file = diskCache.get(imagePath);
        if (file.exists())
            file.delete();
    }

    public void clearMemoryCache() {
        imageCacheBase.getImageLoader().clearMemoryCache();
    }

    public void clearMemoryCache(@NonNull String imagePath) {
        MemoryCache memoryCache = imageCacheBase.getImageLoader().getMemoryCache();
        memoryCache.remove(imagePath);
    }

    private void renderImage(ImageView imageView, String path, int defaultImage, DimenInfo dimenInfo) {
        DisplayImageOptions displayImageOptions;
        if (Validator.isValid(dimenInfo))
            displayImageOptions = imageCacheBase.defaultDIO(dimenInfo);
        else
            displayImageOptions = imageCacheBase.defaultDIO();

        if (Validator.isValid(path)) {
            if (path.startsWith("http") || path.startsWith("https")) {
                renderNormal(imageView, path, defaultImage, displayImageOptions);
            } else if (path.startsWith(IMAGE_DRAWABLE) || path.startsWith(IMAGE_CONTENT) || path.startsWith(IMAGE_ASSETS) || path.startsWith(IMAGE_FILE)) {
                if (!Validator.isValid(imageView.getTag()) || !imageView.getTag().equals(path)) {
                    imageCacheBase.getImageLoader().displayImage(path, new ImageViewAware(imageView, false), displayImageOptions);
                    imageView.setTag(path);
                }
            } else {
                renderDefault(imageView, defaultImage, displayImageOptions);
            }
        } else {
            renderDefault(imageView, defaultImage, displayImageOptions);
        }
    }

    private void renderImage(ImageView imageView, InputStream inputStream, String key, int defaultImage, DimenInfo dimenInfo) {
        DisplayImageOptions displayImageOptions;
        if (Validator.isValid(dimenInfo))
            displayImageOptions = imageCacheBase.defaultDIO(dimenInfo);
        else
            displayImageOptions = imageCacheBase.defaultDIO();

        renderInputStream(imageView, inputStream, key, defaultImage, displayImageOptions);
    }

    private void renderRoundedCorner(ImageView imageView, String path, int defaultImage, DimenInfo dimenInfo) {
        DisplayImageOptions displayImageOptions;
        if (Validator.isValid(dimenInfo))
            displayImageOptions = imageCacheBase.roundedCornerDIO(dimenInfo);
        else
            displayImageOptions = imageCacheBase.roundedCornerDIO();

        renderNormal(imageView, path, defaultImage, displayImageOptions);
    }

    private void renderCircular(ImageView imageView, String path, int defaultImage, DimenInfo dimenInfo) {
        DisplayImageOptions displayImageOptions;
        if (Validator.isValid(dimenInfo))
            displayImageOptions = imageCacheBase.avatarDIO(dimenInfo);
        else
            displayImageOptions = imageCacheBase.avatarDIO();

        renderNormal(imageView, path, defaultImage, displayImageOptions);
    }

    private void renderCircular(ImageView imageView, String path, int defaultImage, int strokeColor, DimenInfo dimenInfo) {
        strokeColor = ColorUtil.getColor(context, strokeColor == -1 ? R.color.white : strokeColor);

        DisplayImageOptions displayImageOptions;
        if (Validator.isValid(dimenInfo))
            displayImageOptions = imageCacheBase.avatarDIO(dimenInfo, strokeColor);
        else
            displayImageOptions = imageCacheBase.avatarDIO(strokeColor);

        renderNormal(imageView, path, defaultImage, displayImageOptions);
    }

    private void renderNormal(ImageView imageView, String path, int defaultImage, DisplayImageOptions displayImageOptions) {
        if (Validator.isValid(path) && !path.equals("")) {
            if (!Validator.isValid(imageView.getTag()) || !imageView.getTag().equals(path)) {
                imageCacheBase.getImageLoader().displayImage(path, new ImageViewAware(imageView, false), displayImageOptions);
                imageView.setTag(path);
            }
        } else {
            renderDefault(imageView, defaultImage, displayImageOptions);
        }
    }

    private void renderInputStream(ImageView imageView, InputStream inputStream, String key, int defaultImage, DisplayImageOptions displayImageOptions) {
        String tag = IMAGE_STREAM + key;

        if (Validator.isValid(inputStream)) {
            if (!Validator.isValid(imageView.getTag()) || !imageView.getTag().equals(tag)) {
                imageCacheBase.getImageLoader().displayImage(tag, new ImageViewAware(imageView, false), displayImageOptions);
                imageView.setTag(tag);
            }
        } else {
            renderDefault(imageView, defaultImage, displayImageOptions);
        }
    }

    private void renderDefault(ImageView imageView, int defaultImage, DisplayImageOptions displayImageOptions) {
        if (!Validator.isValid(imageView.getTag()) || !imageView.getTag().equals(defaultImage)) {
            Drawable drawable = DrawableUtil.getDrawable(context, defaultImage);
            if (drawable instanceof BitmapDrawable) {
                imageCacheBase.getImageLoader().displayImage(IMAGE_DRAWABLE + defaultImage, new ImageViewAware(imageView, false), displayImageOptions);
            } else {
                VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(context.getResources(), defaultImage, null);
                Bitmap bitmap = DrawableUtil.getBitmap(vectorDrawableCompat);

                displayImageOptions.getDisplayer().display(bitmap, new ImageViewAware(imageView, false), LoadedFrom.DISC_CACHE);
            }

            imageView.setTag(defaultImage);
        }
    }
}
