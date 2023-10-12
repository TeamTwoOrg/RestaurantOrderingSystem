package com.teamtwo.kiosk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class PaySuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ImageView dancing_muji;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);

        dancing_muji = findViewById(R.id.dancing_muji);
        Glide.with(this).load(R.drawable.dancing_muji).into(dancing_muji);


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
                "https://port-0-kiosk-server-euegqv2blnemb8x8.sel5.cloudtype.app/order/add",
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