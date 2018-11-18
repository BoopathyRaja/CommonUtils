package com.br.commonutils.helper.imagecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.br.commonutils.R;
import com.br.commonutils.data.common.DimenInfo;
import com.br.commonutils.util.DeviceUtil;
import com.br.commonutils.validator.Validator;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.InputStream;

class ImageCacheBase {

    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private int width;
    private int height;
    private File fileCacheDirectory;
    private int cacheSize;

    private ImageLoader imageLoader;

    protected ImageCacheBase(Context context) {
        width = DeviceUtil.getDeviceWidth(context);
        height = DeviceUtil.getDeviceHeight(context);
        fileCacheDirectory = StorageUtils.getCacheDirectory(context);
        cacheSize = maxMemory / 8; // Use 1/8th of the available memory for this memory cache

        imageLoader = imageLoader(context);
    }

    private ImageLoader imageLoader(Context context) {
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(width, height)
                .diskCacheExtraOptions(width, height, null)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(cacheSize))
                .memoryCacheSize(cacheSize)
                .memoryCacheSizePercentage(8)
                .diskCache(new UnlimitedDiskCache(fileCacheDirectory))
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new CustomBaseImageDownloader(context))
                .imageDecoder(new BaseImageDecoder(false))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(imageLoaderConfiguration);
//        ImageLoader.getInstance().denyNetworkDownloads(true);
        return imageLoader;
    }

    protected ImageLoader getImageLoader() {
        return imageLoader;
    }

    protected void refreshImageLoader(@NonNull Context context) {
        getImageLoader().destroy();
        imageLoader = imageLoader(context);
    }

    protected DisplayImageOptions defaultDIO() {
        return processImage(null, null, new SimpleBitmapDisplayer());
    }

    protected DisplayImageOptions defaultDIO(DimenInfo dimenInfo) {
        return processImage(null, dimenInfo, new SimpleBitmapDisplayer());
    }

    protected DisplayImageOptions defaultDIO(InputStream inputStream, DimenInfo dimenInfo) {
        return processImage(inputStream, dimenInfo, new SimpleBitmapDisplayer());
    }

    protected DisplayImageOptions roundedCornerDIO() {
        return processImage(null, null, new RoundedBitmapDisplayer(10));
    }

    protected DisplayImageOptions roundedCornerDIO(DimenInfo dimenInfo) {
        return processImage(null, dimenInfo, new RoundedBitmapDisplayer(10));
    }

    protected DisplayImageOptions roundedCornerDIO(InputStream inputStream, DimenInfo dimenInfo) {
        return processImage(inputStream, dimenInfo, new RoundedBitmapDisplayer(10));
    }

    protected DisplayImageOptions avatarDIO() {
        return processImage(null, null, getCircularDisplayer(10, Color.WHITE));
    }

    protected DisplayImageOptions avatarDIO(DimenInfo dimenInfo) {
        return processImage(null, dimenInfo, getCircularDisplayer(10, Color.WHITE));
    }

    protected DisplayImageOptions avatarDIO(InputStream inputStream, DimenInfo dimenInfo) {
        return processImage(inputStream, dimenInfo, getCircularDisplayer(10, Color.WHITE));
    }

    protected DisplayImageOptions avatarDIO(int strokeColor, int backgroundColor) {
        return processImage(null, null, getCustomCircularDisplayer(10, strokeColor, backgroundColor));
    }

    protected DisplayImageOptions avatarDIO(DimenInfo dimenInfo, int strokeColor, int backgroundColor) {
        return processImage(null, dimenInfo, getCustomCircularDisplayer(10, strokeColor, backgroundColor));
    }

    protected DisplayImageOptions avatarDIO(InputStream inputStream, DimenInfo dimenInfo, int strokeColor, int backgroundColor) {
        return processImage(inputStream, dimenInfo, getCustomCircularDisplayer(10, strokeColor, backgroundColor));
    }

    private DisplayImageOptions processImage(InputStream inputStream, final DimenInfo dimenInfo, BitmapDisplayer bitmapDisplayer) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        DisplayImageOptions.Builder displayer = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_onloading)
//                .showImageForEmptyUri(R.drawable.)
//                .showImageOnFail(R.drawable.)
                .resetViewBeforeLoading(false)
//                .delayBeforeLoading(1000)
                .cacheInMemory(true)
                .cacheOnDisk(true) // Shows onProgressUpdate if TRUE
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .decodingOptions(options)
                .displayer(bitmapDisplayer);

        if (Validator.isValid(dimenInfo)) {
            displayer.postProcessor(bmp -> {
                try {
                    return Bitmap.createScaledBitmap(bmp, dimenInfo.getWidth(), dimenInfo.getHeight(), false);
                } catch (OutOfMemoryError e) {
                    // Add following line in AndroidManifest.xml
                    // Enable largeHeap
                    // <application android:largeHeap="true"
                    return bmp;
                }
            });
        }

        if (Validator.isValid(inputStream))
            displayer.extraForDownloader(inputStream);

        return displayer.build();
    }

    private BitmapDisplayer getCircularDisplayer(int strokeWidth, int strokeColor) {
        return new CircleBitmapDisplayer(strokeColor, strokeWidth);
    }

    private BitmapDisplayer getCustomCircularDisplayer(int strokeWidth, int strokeColor, int backgroundColor) {
        return new CustomCircleBitmapDisplayer(strokeColor, strokeWidth, backgroundColor);
    }
}
