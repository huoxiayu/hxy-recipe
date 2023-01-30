package com.hxy.recipe.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
public class OkHttpStart {

    public static void main(String[] args) {
        String url = "http://localhost:8088/api/hello";

        HttpClients.get(url, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                log.info("resp header: {}, body: {}", response, response.body().string());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("e: {}", e);
            }
        });

    }

}
