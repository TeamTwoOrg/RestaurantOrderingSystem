package com.example.kiosk;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.loader.content.AsyncTaskLoader;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ManageActivity extends AppCompatActivity {

    private ListView Allorderlist; // 주문 리스트
    private Button productBtn; // 상품보기 버튼

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private boolean isRunning = false;

    // 일정 시간 간격으로 주문 데이터를 로드하는 메서드 호출
    private void startLoadingData() {
        if (!isRunning) {
            isRunning = true;
            runnable = new Runnable() {
                @Override
                public void run() {
                    new FetchProductDataTask().execute();
                    handler.postDelayed(this, 1000); // 10초마다 호출
                }
            };
            handler.post(runnable);
        }
    }

    // 일정 시간 간격으로 호출 중지
    private void stopLoadingData() {
        if (isRunning) {
            handler.removeCallbacks(runnable);
            isRunning = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLoadingData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        startLoadingData();
    }

    // 주문 상태, 테이블 번호, 주문 내역, 주문 시간을 저장할 클래스 생성
    private class OrderInfo {
        String status;
        String tableNum;
        String owner;
        String createdAt;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage);

        startLoadingData();
        productBtn = findViewById(R.id.product_btn); // 상품보기 버튼


        // 상품보기 버튼 클릭 리스너
        productBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                productBtn.setBackgroundResource(R.drawable.darker_button_background);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        productBtn.setBackgroundResource(R.drawable.button_round);

                    }
                }, 200);
                // productActivity로 전환하기 위한 intent 생성
                Intent intent = new Intent(ManageActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
    }

    // 주문 전체보기 리스트 불러오기
    private class FetchProductDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            // POST 요청에 필요한 JSON 데이터 생성
            JSONObject jsonBody = new JSONObject();

            // 로그인 정보 불러오기
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
                    .url("https://sm-kiosk.kro.kr/order")
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
                        ManageActivity.this,
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
                JSONArray ordersArray = dataJson.getJSONArray("orders");

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
        LinearLayout allOrderView = findViewById(R.id.orderZone);
        allOrderView.removeAllViews();

        Typeface customFont = ResourcesCompat.getFont(this, R.font.gmarketsanslight);

        for(int i=menuList.size()-1; i>=0; i--) { // 최신 주문이 위로
            LinearLayout newLayout = new LinearLayout(this);
            LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            newLayoutParams.setMargins(30, 0, 30, 0);
            newLayout.setLayoutParams(newLayoutParams);
            newLayout.setPadding(130, 30, 0, 30);
            newLayout.setBackgroundResource(R.drawable.bottom_border);


            // 주문 상태, 테이블 번호, 주문 내역, 주문 시간
            LinearLayout textLayout = new LinearLayout(this);
            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );

            textLayout.setGravity(Gravity.CENTER_VERTICAL);
            textLayout.setLayoutParams(textLayoutParams);
            textLayoutParams.setMargins(60, 0, 0, 0);
            textLayout.setOrientation(LinearLayout.HORIZONTAL);

            int textTopPadding = 20; // 각 텍스트 사이의 간격을 조절하는 값

            // 주문 상태
            TextView statusText = new TextView(this);
            statusText.setId(View.generateViewId());
            statusText.setLayoutParams(new LinearLayout.LayoutParams(
                    0, // 가로 가중치를 1로 설정하여 화면을 꽉 채우도록 함
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1 // 가로 가중치를 1로 설정하여 화면을 꽉 채우도록 함
            ));
            statusText.setPadding(0, 14, 0, 14);
            statusText.setTextSize(28);
            statusText.setTextColor(Color.WHITE);
            statusText.setTypeface(customFont);

            String status = menuList.get(i).getString("status");
            if(status.equals("0")){
                statusText.setText("주문 대기");
            }else if(status.equals("1")){
                statusText.setText("주문 완료");
            }else
                statusText.setText("주문 취소");

            Log.d("ok","ok");
            textLayout.addView(statusText);

            // 테이블 번호
            TextView tableText = new TextView(this);
            tableText.setLayoutParams(new LinearLayout.LayoutParams(
                    0, // 가로 가중치를 1로 설정하여 화면을 꽉 채우도록 함
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1 // 가로 가중치를 1로 설정하여 화면을 꽉 채우도록 함
            ));
            tableText.setPadding(80, 14, 0, 14);
            tableText.setTextSize(28);
            tableText.setTextColor(Color.WHITE);
            tableText.setTypeface(customFont);
            tableText.setText(menuList.get(i).getString("tableNum"));
            textLayout.addView(tableText);

            // 주문 내역
            TextView nameText = new TextView(this);
            nameText.setLayoutParams(new LinearLayout.LayoutParams(
                    0, // 가로 가중치를 1로 설정하여 화면을 꽉 채우도록 함
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1 // 가로 가중치를 1로 설정하여 화면을 꽉 채우도록 함
            ));
            nameText.setPadding(40, 14, 0, 14);
            nameText.setTextSize(28);
            nameText.setTextColor(Color.WHITE);
            nameText.setTypeface(customFont);
            nameText.setText(menuList.get(i).getJSONObject("menu").getString("name"));
            textLayout.addView(nameText);

            // 주문 시간
            TextView timeText = new TextView(this);
            timeText.setLayoutParams(new LinearLayout.LayoutParams(
                    0, // 가로 가중치를 1로 설정하여 화면을 꽉 채우도록 함
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1 // 가로 가중치를 1로 설정하여 화면을 꽉 채우도록 함
            ));
            timeText.setPadding(40, 14, 0, 14);
            timeText.setTextSize(28);
            timeText.setTextColor(Color.WHITE);
            timeText.setTypeface(customFont);
            String time = menuList.get(i).getString("createdAt").substring(11, 19);

            timeText.setText(time);
            textLayout.addView(timeText);


            // 주문 취소 버튼 레이아웃 파라미터
            LinearLayout.LayoutParams cancelButtonParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cancelButtonParams.setMargins(0, 0, 10, 0); // 마진 설정

            // 주문 취소 버튼
            Button cancelButton = new Button(this);
            cancelButton.setLayoutParams(cancelButtonParams); // 레이아웃 파라미터 설정
            cancelButton.setText("주문 취소");
            cancelButton.setTextColor(Color.WHITE);
            cancelButton.setBackgroundColor(Color.RED);
            cancelButton.setTag(menuList.get(i).getString("_id"));
            // 클릭 리스너 설정
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String orderId = v.getTag().toString();
                    changeOrder(orderId, -1);
                    new FetchProductDataTask().execute();
                }
            });
            textLayout.addView(cancelButton);

            // 주문 완료 버튼
            Button completeButton = new Button(this);
            completeButton.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            completeButton.setText("주문 완료");
            completeButton.setTextColor(Color.WHITE);
            completeButton.setBackgroundColor(Color.parseColor("#5d8a1e"));
            completeButton.setTag(menuList.get(i).getString("_id"));
            // 클릭 리스너 설정
            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String orderId = v.getTag().toString();
                    changeOrder(orderId, 1);
                    new FetchProductDataTask().execute();
                }
            });
            textLayout.addView(completeButton);
            newLayout.addView(textLayout);
            allOrderView.addView(newLayout);
        }
    }

    public void changeOrder(String orderId, int status) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", LoginActivity.cur_id);
            jsonBody.put("password", LoginActivity.cur_pw);
            jsonBody.put("orderId", orderId);
            jsonBody.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCommunicationHelper.sendRequest(
                "https://sm-kiosk.kro.kr/order/changeStatus",
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


}
