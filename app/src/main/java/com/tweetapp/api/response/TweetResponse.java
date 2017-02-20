package com.tweetapp.api.response;

import org.joda.time.DateTime;

/**
 * Created by Miguel Bronzovic.
 */
public class TweetResponse {

    public long id;

    public DateTime created_at;

    public String text;

    public TweetEntityResponse entities;

    public TweetUserResponse user;
}
