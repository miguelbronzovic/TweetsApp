package com.tweetapp;

import android.app.Application;

import com.tweetapp.api.ApiModule;
import com.tweetapp.helper.PicassoHelper;
import com.tweetapp.helper.PreferenceHelper;
import com.tweetapp.helper.ResourceHelper;

import timber.log.Timber;

/**
 * Created by Miguel Bronzovic.
 */
public class TweetSearchApplication extends Application {

    public static TweetSearchApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        ApiModule.init();

        PreferenceHelper.init(this);

        ResourceHelper.init(this);

        PicassoHelper.init();
    }
}
