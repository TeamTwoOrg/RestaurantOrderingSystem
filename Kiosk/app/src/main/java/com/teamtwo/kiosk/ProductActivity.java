package com.teamtwo.kiosk;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductActivity extends AppCompatActivity{
    private Button mealLook; // 카테고리 식사 버튼
    private Button snackLook; // 카테고리 안주 버튼
    private Button LiquorLook; // 카테고리 주류 버튼
    private Button beverageLook; // 카테고리 음료 버튼
    private Button saleslook; // 매출보기 버튼
    private Button productAll_btn;
    private Button addButton;

    private String categoryView;
    public static ArrayList<JSONObject> menuList;

    @Override
    protected void onResume() {
        super.onResume();
        new FetchProductDataTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        categoryView = "식사";

        new FetchProductDataTask().execute();
        productAll_btn = findViewById(R.id.productAll_btn);
        addButton = findViewById(R.id.addButton);

        mealLook = findViewById(R.id.meal_look); // 카테고리 식사 버튼
        snackLook = findViewById(R.id.snack_look); // 카테고리 안주 버튼
        LiquorLook = findViewById(R.id.Liquor_look); // 카테고리 주류 버튼
        beverageLook = findViewById(R.id.beverage_look); // 카테고리 음료 버튼
        saleslook = findViewById(R.id.saleslook); // 매출보기 버튼

        // 처음에 전체보기 버튼 색 채워짐
        mealLook.setBackgroundResource(R.drawable.darker_button_half_background);

        // 식사 버튼 클릭 리스너
        mealLook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                categoryView = "식사";

                mealLook.setBackgroundResource(R.drawable.darker_button_half_background);

                // 주문대기 버튼을 제외한 다른 버튼들의 배경색을 원래대로 되돌림
                snackLook.setBackgroundResource(R.drawable.button_round_half);
                LiquorLook.setBackgroundResource(R.drawable.button_round_half);
                beverageLook.setBackgroundResource(R.drawable.button_round_half);
                productAll_btn.setBackgroundResource(R.drawable.button_round_half);
                try {
                    spreadOrderData(ProductActivity.menuList);
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        });

        // 안주 버튼 클릭 리스너
        snackLook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                categoryView = "안주";
                snackLook.setBackgroundResource(R.drawable.darker_button_half_background);

                // 주문대기 버튼을 제외한 다른 버튼들의 배경색을 원래대로 되돌림
                mealLook.setBackgroundResource(R.drawable.button_round_half);
                productAll_btn.setBackgroundResource(R.drawable.button_round_half);
                LiquorLook.setBackgroundResource(R.drawable.button_round_half);
                beverageLook.setBackgroundResource(R.drawable.button_round_half);
                try {
                    spreadOrderData(ProductActivity.menuList);
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }


            }
        });

        // 주류 버튼 클릭 리스너
        LiquorLook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                categoryView = "주류";
                LiquorLook.setBackgroundResource(R.drawable.darker_button_half_background);

                // 주문대기 버튼을 제외한 다른 버튼들의 배경색을 원래대로 되돌림
                mealLook.setBackgroundResource(R.drawable.button_round_half);
                productAll_btn.setBackgroundResource(R.drawable.button_round_half);
                beverageLook.setBackgroundResource(R.drawable.button_round_half);
                snackLook.setBackgroundResource(R.drawable.button_round_half);
                try {
                    spreadOrderData(ProductActivity.menuList);
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        });

        // 음료 버튼 클릭 리스너
        beverageLook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                categoryView = "음료";
                beverageLook.setBackgroundResource(R.drawable.darker_button_half_background);

                mealLook.setBackgroundResource(R.drawable.button_round_half);
                productAll_btn.setBackgroundResource(R.drawable.button_round_half);
                snackLook.setBackgroundResource(R.drawable.button_round_half);
                LiquorLook.setBackgroundResource(R.drawable.button_round_half);
                try {
                    spreadOrderData(ProductActivity.menuList);
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        });

        // 상품 목록
        productAll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "상품 목록" 버튼이 클릭되었을 때 동작
                // 상품 목록 화면을 표시하는 로직을 추가
                // 예시: Intent로 다음 화면으로 이동
                productAll_btn.setBackgroundResource(R.drawable.darker_button_half_diff_background);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        productAll_btn.setBackgroundResource(R.drawable.button_round_half);

                    }
                }, 200);

                Intent intent = new Intent(ProductActivity.this, ProductListActivity.class);
                startActivity(intent);

            }
        });

        // 매출 전표 보기
        saleslook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "상품 목록" 버튼이 클릭되었을 때 동작
                // 상품 목록 화면을 표시하는 로직을 추가
                // 예시: Intent로 다음 화면으로 이동
                saleslook.setBackgroundResource(R.drawable.darker_button_half_diff_background);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saleslook.setBackgroundResource(R.drawable.button_round_half);

                    }
                }, 200);

                Intent intent = new Intent(ProductActivity.this, SalesActivity.class);
                startActivity(intent);

            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "+" 버튼이 클릭되었을 때 동작
                // 상품을 추가하는 로직을 추가
                // 예시: Toast 메시지를 표시
                addButton.setBackgroundResource(R.drawable.darker_button_background);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addButton.setBackgroundResource(R.drawable.button_round);

                    }
                }, 200);
                Intent intent = new Intent(ProductActivity.this, AddMenuPopUp.class);
                startActivity(intent);
            }
        });
    }

    // 상품보기 리스트 불러오기
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
                    .url("https://port-0-kiosk-server-euegqv2blnemb8x8.sel5.cloudtype.app/menu")
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
                        ProductActivity.this,
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
                ProductActivity.menuList = menuList; // 전역변수에 저장.
                spreadOrderData(menuList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void spreadOrderData(ArrayList<JSONObject> menuList) throws JSONException {
        LinearLayout allOrderView = findViewById(R.id.switching_zone);
        allOrderView.removeAllViews();

        Typeface customFont = ResourcesCompat.getFont(this, R.font.gmarketsanslight);

        for(int i=0; i<menuList.size(); i++) {

            String category = menuList.get(i).getString("category1");
            if(categoryView.equals(category) == false){
                continue;
            }

            LinearLayout newLayout = new LinearLayout(this);
            LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            newLayoutParams.setMargins(30, 0, 30, 0);
            newLayout.setLayoutParams(newLayoutParams);
            newLayout.setPadding(00, 30, 0, 30);
            newLayout.setBackgroundResource(R.drawable.bottom_border);


            // 상품 이미지 (Glide 사용)
            ImageView productImage = new ImageView(this);
            productImage.setId(View.generateViewId());
            productImage.setLayoutParams(new LinearLayout.LayoutParams(
                    400, // 이미지 너비를 원하는 값으로 설정
                    400  // 이미지 높이를 원하는 값으로 설정
            ));
            Glide.with(this)
                    .load(menuList.get(i).getString("imageURL"))
                    .into(productImage);
            newLayout.addView(productImage);

            // 이름, 가격, 추가 설명
            LinearLayout textLayout = new LinearLayout(this);
            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            textLayout.setGravity(Gravity.CENTER_VERTICAL);
            textLayout.setLayoutParams(textLayoutParams);
            textLayoutParams.setMargins(30, 0, 0, 0);
            textLayout.setOrientation(LinearLayout.VERTICAL);

            int textTopPadding = 20; // 각 텍스트 사이의 간격을 조절하는 값

            // 이름
            TextView nameText = new TextView(this);
            nameText.setId(View.generateViewId());
            nameText.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            nameText.setPadding(0, 14, 0, 14);
            nameText.setTextSize(28);
            nameText.setTextColor(Color.WHITE);
            nameText.setTypeface(customFont);
            nameText.setText("상품 이름: " + menuList.get(i).getString("name"));
            textLayout.addView(nameText);

            // 가격
            TextView priceText = new TextView(this);
            priceText.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            priceText.setPadding(0, 14, 0, 14);
            priceText.setTextSize(28);
            priceText.setTextColor(Color.WHITE);
            priceText.setTypeface(customFont);
            priceText.setText("상품 가격: " + menuList.get(i).getString("price"));
            textLayout.addView(priceText);

            // 추가 설명
            TextView subText = new TextView(this);
            subText.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            subText.setPadding(0, 14, 0, 14);
            subText.setTextSize(28);
            subText.setTextColor(Color.WHITE);
            subText.setTypeface(customFont);
            subText.setText("상품 설명: " + menuList.get(i).getString("text"));
            textLayout.addView(subText);


            newLayout.addView(textLayout);


            allOrderView.addView(newLayout);
        }
    }

}