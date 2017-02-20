package com.tweetapp.helper;

import com.tweetapp.BuildConfig;
import com.tweetapp.TweetSearchApplication;
import com.tweetapp.api.ApiModule;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import static com.tweetapp.Constants.DISK_CACHE_SIZE;

/**
 * Created by Miguel Bronzovic.
 */
public final class PicassoHelper {
    private static Picasso INSTANCE;

    private PicassoHelper() {
    }

    public static void init() {
        final LruCache lruCache = new LruCache(DISK_CACHE_SIZE);

        INSTANCE = new Picasso.Builder(TweetSearchApplication.INSTANCE)
                .downloader(ApiModule.getOkHttp3Downloader())
                .indicatorsEnabled(BuildConfig.DEBUG ? true : false)
                .loggingEnabled(true)
                .memoryCache(lruCache)
                .build();
    }

    public static Picasso getInstance() {
        return INSTANCE;
    }
}
