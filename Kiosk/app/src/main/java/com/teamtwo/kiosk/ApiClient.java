package com.teamtwo.kiosk;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ApiClient {

    private static final String BASE_URL = "https://port-0-kiosk-server-euegqv2blnemb8x8.sel5.cloudtype.app/";

    public static void sendPostRequest(JSONObject jsonBody) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                parseResponse(responseBody);
            } else {
                // Handle error
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseResponse(String responseBody) {
        try {
            MenuForUser mfu = new MenuForUser();
            JSONObject jsonResponse = new JSONObject(responseBody);
            mfu.setName(jsonResponse.getString("name"));
            // 이제 'name' 필드 값을 사용하면 됩니다.
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
