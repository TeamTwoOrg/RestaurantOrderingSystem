package com.teamtwo.kiosk;


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
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public void remove_menu(String name) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", LoginActivity.cur_id);
            jsonBody.put("password", LoginActivity.cur_pw);
            jsonBody.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCommunicationHelper.sendRequest(
                "https://sm-kiosk.kro.kr/menu/remove",
                ServerCommunicationHelper.HttpMethod.POST,
                jsonBody,
                new ServerCommunicationHelper.ResultCallback() {
                    @Override
                    public void onSuccess(String responseBody) {
                        System.out.println("변경됨");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("xxx");
                    }
                }
        );
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

            } catch (JSONException e) {

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
            defaultLayout.weight = 0.5f; // weight를 1로 설정

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
            numText.setGravity(Gravity.CENTER_VERTICAL);
            numText.setText(String.valueOf(i+1));
            newLayOut.addView(numText);


            // 카테고리
            TextView cateText = new TextView(this);
            cateText.setLayoutParams(defaultLayout);
            cateText.setTextSize(28);
            cateText.setTextColor(Color.WHITE);
            cateText.setTypeface(customFont);
            cateText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            cateText.setText(menuList.get(i).getString("category1"));
            newLayOut.addView(cateText);



            // 이름
            LinearLayout.LayoutParams nameTextLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            TextView nameText = new TextView(this);
            nameText.setLayoutParams(defaultLayout);
            nameText.setTextSize(24);
            nameText.setTextColor(Color.WHITE);
            nameText.setTypeface(customFont);
            nameText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            nameText.setText(menuList.get(i).getString("name"));
            nameTextLayoutParams.setMargins(0, 0, 100, 0); // 오른쪽 마진 설정
            nameText.setPadding(0, 0, 100, 0); // 오른쪽 패딩 설정
            newLayOut.addView(nameText);

            // 가격
            TextView timeText = new TextView(this);
            timeText.setLayoutParams(defaultLayout);
            timeText.setTextSize(28);
            timeText.setTextColor(Color.WHITE);
            timeText.setTypeface(customFont);
            timeText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            timeText.setText(menuList.get(i).getString("price"));
            newLayOut.addView(timeText);

            // 추가 설명
            TextView subText = new TextView(this);
            subText.setLayoutParams(defaultLayout);
            subText.setTextSize(17);
            subText.setTextColor(Color.WHITE);
            subText.setTypeface(customFont);
            subText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            subText.setText(menuList.get(i).getString("text"));
            newLayOut.addView(subText);

            Space space2 = new Space(this);
            space2.setLayoutParams(spaceLayout);
            newLayOut.addView(space2);

            // 메뉴 삭제 버튼
            Button deleteButton = new Button(this);
            deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                    80,
                    15
            ));


            deleteButton.setText("삭제");  // 버튼 텍스트 설정
            deleteButton.setTextSize(20); // 버튼 텍스트 크기 설정
            deleteButton.setTextColor(Color.WHITE);
            deleteButton.setBackgroundColor(Color.RED);
            int finalI = i;
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String menuName = menuList.get(finalI).getString("name"); // 삭제할 메뉴 이름 가져오기
                        remove_menu(menuName); // 메뉴 삭제 함수 호출

                        // 메뉴 항목을 menuList에서 삭제하고 UI를 업데이트합니다.
                        if (finalI >= 0 && finalI < menuList.size()) {
                            menuList.remove(finalI);
                            spreadOrderData(menuList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            LinearLayout.LayoutParams deleteButtonLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            deleteButtonLayoutParams.setMargins(0, 0, 130, 0); // 왼쪽 마진 설정
            deleteButton.setLayoutParams(deleteButtonLayoutParams);


            newLayOut.addView(deleteButton);

            // 마무리
            allOrderView.addView(newLayOut);
        }
    }
}
