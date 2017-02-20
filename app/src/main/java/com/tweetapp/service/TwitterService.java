package com.tweetapp.service;

import com.tweetapp.api.APIClient;
import com.tweetapp.api.response.OAuthTokenResponse;
import com.tweetapp.api.response.TweetResponse;
import com.tweetapp.api.response.TweetSearchResponse;
import com.tweetapp.data.TweetEntity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Miguel Bronzovic.
 */
public final class TwitterService {

    private APIClient apiClient;

    public TwitterService(APIClient APIClientImpl) {
        apiClient = APIClientImpl;
    }

    /**
     * Gets the access token from the API Client. </br>
     *
     * @return String access token for subsequent requests
     */
    public Observable<String> getToken() {
        return apiClient.getBearerToken().flatMap(new Func1<OAuthTokenResponse, Observable<? extends String>>() {
            @Override
            public Observable<? extends String> call(OAuthTokenResponse oAuthTokenResponse) {
                return Observable.just(oAuthTokenResponse.access_token);
            }
        });
    }

    /**
     * Search for a list of {@link TweetEntity} in the API Client. </br>
     *
     * @param searchText query
     * @return {@link Observable<List<TweetEntity>>} list
     */
    public Observable<List<TweetEntity>> getTweets(final String searchText) {
        if (searchText == null || searchText.isEmpty())
            throw new NullPointerException("searchText is empty or null");

        return apiClient.getTweets(searchText).flatMap(new Func1<TweetSearchResponse, Observable<? extends List<TweetEntity>>>() {
            @Override
            public Observable<? extends List<TweetEntity>> call(TweetSearchResponse tweetSearchResponse) {
                final List<TweetEntity> tweetEntitiesList = new ArrayList<>();
                for (TweetResponse tweetResponse : tweetSearchResponse.statuses) {
                    final TweetEntity entity = new TweetEntity();
                    entity.id = tweetResponse.id;
                    entity.created_at = tweetResponse.created_at;
                    entity.text = tweetResponse.text;
                    entity.userName = tweetResponse.user.name;
                    entity.userScreenName = String.format("@%s",tweetResponse.user.screen_name);
                    entity.userProfileImageUrl = tweetResponse.user.profile_image_url.replace("_normal", "_bigger");
                    if ((tweetResponse.entities != null) && (tweetResponse.entities.media != null)) {
                        if (!tweetResponse.entities.media.isEmpty()) {
                            entity.mediaUrl = tweetResponse.entities.media.get(0).media_url;
                        }
                    }
                    tweetEntitiesList.add(entity);
                }
                return Observable.just(tweetEntitiesList);
            }
        });
    }
}
