package com.hxy.recipe.okhttp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpClients {

    private static final OkHttpClient CLIENT = new OkHttpClient();

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
