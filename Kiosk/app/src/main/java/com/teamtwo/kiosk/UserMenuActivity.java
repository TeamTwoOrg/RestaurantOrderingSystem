package com.teamtwo.kiosk;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class UserMenuActivity extends AppCompatActivity {
    ImageButton menuImageButton = findViewById(R.id.menu_img);
    TextView menuNameTextView = findViewById(R.id.user_menu_name);
    TextView menuPriceTextView = findViewById(R.id.user_menu_price);
    HorizontalScrollView userMenuScrollView = findViewById(R.id.user_menu_scroll_view);
    LinearLayout fragment_meal_zone = findViewById(R.id.fragment_meal_zone);
    LinearLayout fragment_snack_zone = findViewById(R.id.fragment_snack_zone);
    LinearLayout fragment_liquor_zone = findViewById(R.id.fragment_liquor_zone);
    LinearLayout fragment_beverage_zone = findViewById(R.id.fragment_beverage_zone);
    @Override
    protected void onResume() {
        super.onResume();
        new FetchProductDataTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_for_menu);

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
        LinearLayout user_switching_zone = findViewById(R.id.user_switching_zone);
        user_switching_zone.removeAllViews();

        Typeface customFont = ResourcesCompat.getFont(this, R.font.gmarketsanslight);

        for(int i=0; i<menuList.size(); i++) {
            LinearLayout newLayout = new LinearLayout(this);
            LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            newLayout.setBackgroundColor(0000);

            LinearLayout innerLayout = new LinearLayout(this);
            LinearLayout.LayoutParams innerLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    5
            );
            innerLayout.setBackgroundColor(292828);

            menuImageButton = new ImageButton(this);
            menuImageButton.setId(View.generateViewId());
            menuImageButton.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1
            ));
            Glide.with(this)
                    .load(menuList.get(i).getString("imageURL"))
                    .into(menuImageButton);
            menuNameTextView.setText(menuList.get(i).getString("name"));
            menuNameTextView.setTextColor(getResources().getColor(R.color.white));
            menuNameTextView.setTextSize(20);
            menuNameTextView.setGravity(Gravity.CENTER);
            menuNameTextView.setIncludeFontPadding(false);

            menuPriceTextView.setText(menuList.get(i).getString("price"));
            menuPriceTextView.setTextColor(getResources().getColor(R.color.white));
            menuPriceTextView.setTextSize(20);
            menuPriceTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            menuPriceTextView.setIncludeFontPadding(false);
            menuPriceTextView.setPadding(0,5,0,0);

            innerLayout.addView(menuNameTextView);
            innerLayout.addView(menuPriceTextView);
            newLayout.addView(menuImageButton);
            newLayout.addView(innerLayout);

            LinearLayout midOuterLayout = new LinearLayout(this);
            LinearLayout.LayoutParams midOuterLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            midOuterLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout midInnerLayout = new LinearLayout(this);
            LinearLayout.LayoutParams midInnerLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (float) 0.4
            );
            midInnerLayout.setOrientation(LinearLayout.HORIZONTAL);
            midInnerLayout.setBackgroundColor(0000);
            midInnerLayout.setPadding(15,15,15,15);

            LinearLayout InnerLinearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams InnerLinearLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            midInnerLayout.setOrientation(LinearLayout.HORIZONTAL);
            midInnerLayout.setBackgroundColor(0000);

            LinearLayout mainInnerLinearLayout = new LinearLayout(this);
            FrameLayout.LayoutParams mainInnerLinearLayoutParams = new FrameLayout.LayoutParams(
                    320,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );

            mainInnerLinearLayout.addView(newLayout);
            InnerLinearLayout.addView(mainInnerLinearLayout);
            userMenuScrollView.addView(InnerLinearLayout);
            midInnerLayout.addView(userMenuScrollView);
            midOuterLayout.addView(midInnerLayout);

            if(menuList.get(i).getString("category1").equals("식사")){
                fragment_meal_zone.addView(midOuterLayout);
            }
            else if(menuList.get(i).getString("category1").equals("안주")){
                fragment_snack_zone.addView(midOuterLayout);
            }
            else if(menuList.get(i).getString("category1").equals("주류")){
                fragment_liquor_zone.addView(midOuterLayout);
            }
            else if(menuList.get(i).getString("category1").equals("음료")){
                fragment_beverage_zone.addView(midOuterLayout);
            }

        }
    }
}

