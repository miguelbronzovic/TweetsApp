package com.tweetapp.service;

import com.tweetapp.api.ApiModuleMock;
import com.tweetapp.data.TweetEntity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Miguel Bronzovic.
 */
public class TwitterServiceTest extends ServiceTest {

    public void testTwitterServiceConnect() {
        service.getToken().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                assertNotNull(s);
            }
        });
    }

    public void testSearchTweets() {
        service.getToken().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                ApiModuleMock.getTwitterInMemoryInterceptor().setToken(s);
            }
        });
        
        service.getTweets("#androiddev").subscribe(new Action1<List<TweetEntity>>() {
            @Override
            public void call(List<TweetEntity> tweetEntities) {
                assertNotNull(tweetEntities);
                assertEquals(50, tweetEntities.size());
            }
        });
    }

    public void testSearchTweetsNoResults() {
        service.getToken().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                ApiModuleMock.getTwitterInMemoryInterceptor().setToken(s);
            }
        });

        service.getTweets("#drjeitnfoi").subscribe(new Action1<List<TweetEntity>>() {
            @Override
            public void call(List<TweetEntity> tweetEntities) {
                assertNotNull(tweetEntities);
                assertEquals(0, tweetEntities.size());
            }
        });
    }

    public void testSearchTweetsNoInput() {
        service.getToken();

        try {
            service.getTweets(null);
        } catch (Exception ex) {
            assertEquals(NullPointerException.class, ex.getClass());
            assertEquals("searchText is empty or null", ex.getMessage());
        }
    }
}
