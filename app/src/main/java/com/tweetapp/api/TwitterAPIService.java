package com.tweetapp.api;

import com.tweetapp.api.response.OAuthTokenResponse;
import com.tweetapp.api.response.TweetSearchResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import static com.tweetapp.Constants.BASIC_AUTHORIZATION_ENCODED;
import static com.tweetapp.Constants.USER_AGENT;
import static com.tweetapp.api.constant.UriConstants.OAUTH2_TOKEN;
import static com.tweetapp.api.constant.UriConstants.SEARCH_TWEETS;

/**
 * Created by Miguel Bronzovic.
 */
public interface TwitterAPIService {

    @Headers({
            "Authorization: Basic " + BASIC_AUTHORIZATION_ENCODED,
    "User-Agent: " + USER_AGENT})
    @FormUrlEncoded
    @POST(OAUTH2_TOKEN)
    Observable<OAuthTokenResponse> getBearerToken(@Field("grant_type") String grantType);

    @GET(SEARCH_TWEETS)
    Observable<TweetSearchResponse> getTweets(@Query("q") String searchText, @Query("count") Integer count);
}
