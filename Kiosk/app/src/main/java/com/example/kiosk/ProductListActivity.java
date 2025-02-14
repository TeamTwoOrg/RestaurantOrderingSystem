package com.example.kiosk;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.teamtwo.kiosk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductListActivity extends AppCompatActivity {

//    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        // 서버에서 데이터 가져오는 AsyncTask 실행
        new FetchProductDataTask().execute();
    }

    private class FetchProductDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            // POST 요청에 필요한 JSON 데이터 생성
            JSONObject jsonBody = new JSONObject();


            try {
                jsonBody.put("id", LoginActivity.cur_id);
                jsonBody.put("password", LoginActivity.cur_pw);


                // 필요한 다른 데이터도 추가
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // JSON 데이터를 요청할 데이터로 설정
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    jsonBody.toString()
            );

            Request request = new Request.Builder()
                    .url("https://sm-kiosk.kro.kr/menu")
                    .post(requestBody)
                    .build();

            try {

                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response != null) {
                // 서버로부터 받은 데이터를 이용하여 ListView에 표시
                String[] products = response.split(";"); // 적절한 데이터 형식에 따라 파싱
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        ProductListActivity.this,
                        android.R.layout.simple_list_item_1,
                        products
                );
//                listView.setAdapter(adapter);
                parseJSON(response);
            }
        }

        protected void parseJSON(String response) {
            Log.d("test"," dsfdsfsdfdsfsdfff");
            ArrayList<JSONObject> menuList = new ArrayList<>();

            try {
                JSONObject responseJson = new JSONObject(response);
                JSONObject dataJson = responseJson.getJSONObject("data");
                JSONArray ordersArray = dataJson.getJSONArray("menus");

                for (int i = 0; i < ordersArray.length(); i++) {
                    JSONObject order = ordersArray.getJSONObject(i);
                    menuList.add(order);
                }

                spreadOrderData(menuList);
//                 테스트 출력 코드
//                for (JSONObject menuItem : menuList) {
//                    // JSONObject를 보기 좋게 로그에 출력
//                    String jsonMenuItem = menuItem.toString(4); // 들여쓰기 4칸으로 출력
//                    Log.d("test", jsonMenuItem);
////                                System.out.println(menuItem.getString("imageURL"));
//                }

            } catch (JSONException e) {
//                            System.err.println("실패");
                e.printStackTrace();
            }
        }
    }

    public void spreadOrderData(ArrayList<JSONObject> menuList) throws JSONException {
        LinearLayout allOrderView = findViewById(R.id.allOrderView);
        allOrderView.removeAllViews();

        Typeface customFont = ResourcesCompat.getFont(this, R.font.gmarketsanslight);

        for(int i=0; i<menuList.size(); i++) {
            LinearLayout newLayOut = new LinearLayout(this);
            newLayOut.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    200
            ));
            newLayOut.setOrientation(LinearLayout.HORIZONTAL);
            newLayOut.setGravity(Gravity.CENTER_VERTICAL);// 수직으로 가운데로 정렬
            newLayOut.setBackgroundResource(R.drawable.bottom_border);

            LinearLayout.LayoutParams defaultLayout = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            defaultLayout.weight = 1; // weight를 1로 설정

            LinearLayout.LayoutParams spaceLayout = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            spaceLayout.weight = 0.2f; // weight를 1로 설정

            Space space1 = new Space(this);
            space1.setLayoutParams(spaceLayout);
            newLayOut.addView(space1);

            // 번호
            TextView numText = new TextView(this);
            numText.setLayoutParams(defaultLayout);
            numText.setTextSize(28);
            numText.setTextColor(Color.WHITE);
            numText.setTypeface(customFont);
            numText.setGravity(Gravity.CENTER_HORIZONTAL);
            numText.setText(String.valueOf(i+1));
            newLayOut.addView(numText);


            // 카테고리
            TextView cateText = new TextView(this);
            cateText.setLayoutParams(defaultLayout);
            cateText.setTextSize(28);
            cateText.setTextColor(Color.WHITE);
            cateText.setTypeface(customFont);
            cateText.setGravity(Gravity.CENTER_HORIZONTAL);
            cateText.setText(menuList.get(i).getString("category1"));
            newLayOut.addView(cateText);

            // 이름
            TextView nameText = new TextView(this);
            nameText.setLayoutParams(defaultLayout);
            nameText.setTextSize(28);
            nameText.setTextColor(Color.WHITE);
            nameText.setTypeface(customFont);
            nameText.setGravity(Gravity.CENTER_HORIZONTAL);
            nameText.setText(menuList.get(i).getString("name"));
            newLayOut.addView(nameText);

            // 가격
            TextView timeText = new TextView(this);
            timeText.setLayoutParams(defaultLayout);
            timeText.setTextSize(28);
            timeText.setTextColor(Color.WHITE);
            timeText.setTypeface(customFont);
            timeText.setGravity(Gravity.CENTER_HORIZONTAL);
            timeText.setText(menuList.get(i).getString("price"));
            newLayOut.addView(timeText);

            // 추가 설명
            TextView subText = new TextView(this);
            subText.setLayoutParams(defaultLayout);
            subText.setTextSize(28);
            subText.setTextColor(Color.WHITE);
            subText.setTypeface(customFont);
            subText.setGravity(Gravity.CENTER_HORIZONTAL);
            subText.setText(menuList.get(i).getString("text"));
            newLayOut.addView(subText);

            Space space2 = new Space(this);
            space2.setLayoutParams(spaceLayout);
            newLayOut.addView(space2);

            // 마무리
            allOrderView.addView(newLayOut);
        }
    }
}
