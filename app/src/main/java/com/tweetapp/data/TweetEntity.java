package com.tweetapp.data;

import org.joda.time.DateTime;
import org.parceler.Parcel;

/**
 * Created by Miguel Bronzovic.
 */
@Parcel
public class TweetEntity {

    public long id;

    public DateTime created_at;

    public String text;

    public String userName;

    public String userScreenName;

    public String userProfileImageUrl;

    public String mediaUrl;
}
