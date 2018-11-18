package com.br.commonutils.helper.imagecache;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.br.commonutils.validator.Validator;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PauseOnScrollListener extends RecyclerView.OnScrollListener {

    private final boolean pauseOnScroll;
    private final boolean pauseOnSettling;
    private final RecyclerView.OnScrollListener onScrollListener;
    private ImageLoader imageLoader;

    public PauseOnScrollListener(@NonNull ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnSettling) {
        this(imageLoader, pauseOnScroll, pauseOnSettling, null);
    }

    public PauseOnScrollListener(@NonNull ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnSettling, @NonNull RecyclerView.OnScrollListener onScrollListener) {
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnSettling = pauseOnSettling;
        this.onScrollListener = onScrollListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                imageLoader.resume();
                break;

            case RecyclerView.SCROLL_STATE_DRAGGING:
                if (pauseOnScroll)
                    imageLoader.pause();
                break;

            case RecyclerView.SCROLL_STATE_SETTLING:
                if (pauseOnSettling)
                    imageLoader.pause();
                break;
        }

        if (Validator.isValid(onScrollListener))
            onScrollListener.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (Validator.isValid(onScrollListener))
            onScrollListener.onScrolled(recyclerView, dx, dy);
    }
}
