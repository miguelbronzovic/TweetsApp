package com.tweetapp.api.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Miguel Bronzovic.
 */
public class TwitterInMemoryInterceptor implements Interceptor {
    private String token;

    public TwitterInMemoryInterceptor() {}

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        final String bearerToken = this.token;
        if (bearerToken != null && !bearerToken.isEmpty()) {
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

    public void setToken(String token) {
        this.token = token;
    }
}
