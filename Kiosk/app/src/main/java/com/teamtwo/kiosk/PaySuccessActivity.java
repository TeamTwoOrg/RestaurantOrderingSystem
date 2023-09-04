package com.teamtwo.kiosk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class PaySuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);

        for(String key : ShoppingCartDialog.addInfo.keySet()) {
            int cnt = ShoppingCartDialog.addInfo.get(key)[1];
            for(int i=0; i<cnt; i++) {
                order(key);
            }
        }

        ShoppingCartDialog.cartClear();
    }

    public void order(String name) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", LoginActivity.cur_id);
            jsonBody.put("password", LoginActivity.cur_pw);
            jsonBody.put("name", name);
            jsonBody.put("tableNum", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCommunicationHelper.sendRequest(
                "https://sm-kiosk.kro.kr/order/add",
                ServerCommunicationHelper.HttpMethod.POST,
                jsonBody,
                new ServerCommunicationHelper.ResultCallback() {
                    @Override
                    public void onSuccess(String responseBody) {
                        Log.d("order", "추가됨");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("order", "실패함");
                    }
                }
        );
    }

    public void payEndConfirm(View view) {
        onBackPressed();
    }
}