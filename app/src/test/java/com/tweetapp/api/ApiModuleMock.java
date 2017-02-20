package com.tweetapp.api;

import com.tweetapp.api.interceptor.TwitterInMemoryInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.tweetapp.Constants.OKHTTP_CONNECTION_TIMEOUT;
import static com.tweetapp.Constants.OKHTTP_READ_TIMEOUT;

/**
 * Created by Miguel Bronzovic.
 */
public final class ApiModuleMock {
    private static TwitterInMemoryInterceptor twitterInMemoryInterceptor;
    private static OkHttpClient okHttpClient;

    private ApiModuleMock() {}

    public static void init() {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // we use here a 'memory' interceptor for the token
        twitterInMemoryInterceptor = new TwitterInMemoryInterceptor();

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(OKHTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(twitterInMemoryInterceptor)
                .build();
    }

    /**
     * Gets the In Memory Interceptor's reference. </br>
     *
     * @return {@link TwitterInMemoryInterceptor} instance
     */
    public static TwitterInMemoryInterceptor getTwitterInMemoryInterceptor() {
        return twitterInMemoryInterceptor;
    }

    /**
     * Gets an OkHttpClient reference. </br>
     *
     * @return {@link OkHttpClient} instance
     */
    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
