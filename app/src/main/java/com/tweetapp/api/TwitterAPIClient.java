package com.tweetapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tweetapp.api.response.OAuthTokenResponse;
import com.tweetapp.api.response.TweetSearchResponse;
import com.tweetapp.api.utils.DateTimeTypeConverter;

import org.joda.time.DateTime;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Miguel Bronzovic.
 */
public final class TwitterAPIClient implements APIClient {
    public final static String BASE_URL = "https://api.twitter.com/";

    private TwitterAPIService twitterAPIService;

    /**
     * Builds the Twitter API Client implementation for the App. </br>
     *
     * @param okHttpClient dependency
     */
    public TwitterAPIClient(final OkHttpClient okHttpClient) {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
                .create();

        final RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.create();

        final Retrofit retrofitAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();

        twitterAPIService = retrofitAdapter.create(TwitterAPIService.class);
    }

    public Observable<OAuthTokenResponse> getBearerToken() {
        final String grantType = "client_credentials";

        return twitterAPIService.getBearerToken(grantType);
    }

    public Observable<TweetSearchResponse> getTweets(final String searchText) {
        final Integer count = 50;

        return twitterAPIService.getTweets(searchText, count);
    }
}
