package com.tweetapp.api;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.tweetapp.BuildConfig;
import com.tweetapp.TweetSearchApplication;
import com.tweetapp.api.interceptor.TwitterRequestInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.tweetapp.Constants.DISK_CACHE_SIZE;
import static com.tweetapp.Constants.OKHTTP_CONNECTION_TIMEOUT;
import static com.tweetapp.Constants.OKHTTP_READ_TIMEOUT;

/**
 * Created by Miguel Bronzovic.
 */
public final class ApiModule {
    private static Cache cacheInstance;
    private static OkHttpClient okHttpClient;
    private static OkHttp3Downloader okHttp3Downloader;

    private ApiModule() {}

    public static void init() {
        // Install an HTTP cache in the application cache directory.
        final File cacheDir = new File(TweetSearchApplication.INSTANCE.getCacheDir(), "http");
        cacheInstance = new Cache(cacheDir, DISK_CACHE_SIZE);

        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        final TwitterRequestInterceptor twitterRequestInterceptor = new TwitterRequestInterceptor();

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(OKHTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(twitterRequestInterceptor)
                .cache(cacheInstance)
                .build();

        okHttp3Downloader = new OkHttp3Downloader(okHttpClient);
    }

    /**
     * Gets an OkHttpClient reference. </br>
     *
     * @return {@link OkHttpClient} instance
     */
    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * Gets an OkHttpDownloader reference for Picasso Instance. </br>
     *
     * @return {@link OkHttp3Downloader} instance
     */
    public static OkHttp3Downloader getOkHttp3Downloader() {
        return okHttp3Downloader;
    }
}
