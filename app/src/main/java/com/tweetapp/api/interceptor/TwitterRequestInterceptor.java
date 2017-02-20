package com.tweetapp.api.interceptor;

import android.text.TextUtils;

import com.tweetapp.helper.PreferenceHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.tweetapp.Constants.BEARER_TOKEN;

/**
 * Created by Miguel Bronzovic.
 */
public final class TwitterRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        final String bearerToken = PreferenceHelper.getString(BEARER_TOKEN);
        if (!TextUtils.isEmpty(bearerToken)) {
            try {
                Request newRequest = new Request.Builder()
                        .url(originalRequest.url())
                        .addHeader("Authorization", "Bearer " + bearerToken)
                        .method(originalRequest.method(), originalRequest.body())
                        .build();
                return chain.proceed(newRequest);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return chain.proceed(originalRequest);
    }
}
