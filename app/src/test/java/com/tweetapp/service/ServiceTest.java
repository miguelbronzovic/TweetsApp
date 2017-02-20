package com.tweetapp.service;

import com.tweetapp.api.ApiModuleMock;
import com.tweetapp.api.TwitterAPIClient;

import junit.framework.TestCase;

/**
 * Created by Miguel Bronzovic.
 */
public abstract class ServiceTest extends TestCase {
    protected TwitterService service;

    @Override
    protected void setUp() throws Exception {
        ApiModuleMock.init();

        TwitterAPIClient apiClient = new TwitterAPIClient(ApiModuleMock.getOkHttpClient());

        service = new TwitterService(apiClient);
    }
}
