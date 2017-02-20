package com.tweetapp.api;

import com.tweetapp.api.response.OAuthTokenResponse;
import com.tweetapp.api.response.TweetSearchResponse;

import rx.Observable;

/**
 * Created by Miguel Bronzovic.
 */
public interface APIClient {

    Observable<OAuthTokenResponse> getBearerToken();

    Observable<TweetSearchResponse> getTweets(final String searchText);
}
