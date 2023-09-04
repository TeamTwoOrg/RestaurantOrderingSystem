package com.example.kiosk;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class ServerCommunicationHelper {

    public interface ResultCallback {
        void onSuccess(String responseBody);
        void onFailure(Exception e);
    }

    public static void sendRequest(String url, HttpMethod httpMethod, JSONObject jsonBody, ResultCallback callback) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String requestBody = (jsonBody != null) ? jsonBody.toString() : "";
        RequestBody formBody = RequestBody.create(requestBody, JSON);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url);

        if (httpMethod == HttpMethod.POST) {
            requestBuilder.post(formBody);
        } else if (httpMethod == HttpMethod.GET) {
            requestBuilder.get();
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        runOnUiThread(() -> callback.onSuccess(responseBody));
                    } else {
                        runOnUiThread(() -> callback.onFailure(new Exception("Request failed with code: " + response.code())));
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> callback.onFailure(e));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> callback.onFailure(e));
            }
        });
    }

    private static void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public enum HttpMethod {
        GET,
        POST
    }
}
