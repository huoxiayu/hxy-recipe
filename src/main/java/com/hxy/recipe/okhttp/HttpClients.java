package com.hxy.recipe.okhttp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpClients {

    private static final long TIME_OUT_IN_MILLIS = 500L;
    private static final int MAX_IDLE_CONNECTIONS = 2;
    private static final long KEEP_ALIVE_IN_MILLIS = 30L;

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(TIME_OUT_IN_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(TIME_OUT_IN_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(TIME_OUT_IN_MILLIS, TimeUnit.MILLISECONDS)
            .followRedirects(true)
            .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_IN_MILLIS, TimeUnit.MILLISECONDS))
            .build();

    public static Response get(String url) {
        try {
            return getCall(url).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void get(String url, Callback callback) {
        getCall(url).enqueue(callback);
    }

    private static Call getCall(String url) {
        return CLIENT.newCall(new Request.Builder().url(url).build());
    }

}
