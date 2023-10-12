package com.teamtwo.kiosk;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.ListView;
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
import android.media.MediaPlayer;
public class ManageActivity extends AppCompatActivity {

    private ListView Allorderlist; // 주문 리스트
    private Button productBtn; // 상품보기 버튼
    private Button orderAll; // 전체보기 버튼
    private Button orderPending; // 주문대기 버튼
    private Button orderCanceled; // 주문취소 버튼
    private Button orderCompleted; // 주문완료 버튼
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private boolean isRunning = false;
    private int orderViewSelect;
    public static ArrayList<JSONObject> menuList;

    private MediaPlayer mediaPlayer; // 소리 파일

    // 일정 시간 간격으로 주문 데이터를 로드하는 메서드 호출
    private void startLoadingData() {
        if (!isRunning) {
            isRunning = true;
            runnable = new Runnable() {
                @Override
                public void run() {
                    new FetchProductDataTask().execute();
                    handler.postDelayed(this, 1000); // 1초마다 호출

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
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderViewSelect = 0;
        setContentView(R.layout.manage);

        startLoadingData();

        productBtn = findViewById(R.id.product_btn); // 상품보기 버튼
        orderAll = findViewById(R.id.Order_All); // 전체보기 버튼
        orderPending = findViewById(R.id.Order_Pending); // 주문대기 버튼
        orderCanceled = findViewById(R.id.Order_Canceled); // 주문취소 버튼
        orderCompleted = findViewById(R.id.Order_Completed); // 주문완료 버튼

        // 처음에 주문대기 버튼 색 채워짐
        orderPending.setBackgroundResource(R.drawable.darker_button_half_background);

        // 전체보기 버튼 클릭 리스너
        orderAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                orderViewSelect = 2;
                orderAll.setBackgroundResource(R.drawable.darker_button_half_background);

                // 주문대기 버튼을 제외한 다른 버튼들의 배경색을 원래대로 되돌림
                orderCanceled.setBackgroundResource(R.drawable.button_round_half);
                orderPending.setBackgroundResource(R.drawable.button_round_half);
                orderCompleted.setBackgroundResource(R.drawable.button_round_half);
                productBtn.setBackgroundResource(R.drawable.button_round_half);
                try {
                    spreadOrderData(ManageActivity.menuList);
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        });

        // 주문대기 버튼 클릭 리스너
        orderPending.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                orderViewSelect = 0;
                orderPending.setBackgroundResource(R.drawable.darker_button_half_background);

                // 주문대기 버튼을 제외한 다른 버튼들의 배경색을 원래대로 되돌림
                orderAll.setBackgroundResource(R.drawable.button_round_half);
                orderCanceled.setBackgroundResource(R.drawable.button_round_half);
                orderCompleted.setBackgroundResource(R.drawable.button_round_half);
                productBtn.setBackgroundResource(R.drawable.button_round_half);
                try {
                    spreadOrderData(ManageActivity.menuList);
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }


            }
        });

        // 주문취소 버튼 클릭 리스너
        orderCanceled.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                orderViewSelect = -1;
                orderCanceled.setBackgroundResource(R.drawable.darker_button_half_background);

                // 주문대기 버튼을 제외한 다른 버튼들의 배경색을 원래대로 되돌림
                orderAll.setBackgroundResource(R.drawable.button_round_half);
                orderPending.setBackgroundResource(R.drawable.button_round_half);
                orderCompleted.setBackgroundResource(R.drawable.button_round_half);
                productBtn.setBackgroundResource(R.drawable.button_round_half);
                try {
                    spreadOrderData(ManageActivity.menuList);
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        });

        // 주문완료 버튼 클릭 리스너
        orderCompleted.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                orderViewSelect = 1;
                orderCompleted.setBackgroundResource(R.drawable.darker_button_half_background);

                orderAll.setBackgroundResource(R.drawable.button_round_half);
                orderPending.setBackgroundResource(R.drawable.button_round_half);
                orderCanceled.setBackgroundResource(R.drawable.button_round_half);
                productBtn.setBackgroundResource(R.drawable.button_round_half);
                try {
                    spreadOrderData(ManageActivity.menuList);
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
            }
        });

        // 상품보기 버튼 클릭 리스너
        productBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                productBtn.setBackgroundResource(R.drawable.darker_button_half_diff_background);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        productBtn.setBackgroundResource(R.drawable.button_round_half);
                        // productActivity로 전환하기 위한 intent 생성
                        Intent intent = new Intent(ManageActivity.this, ProductActivity.class);
                        startActivity(intent);
                    }
                }, 30);

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
                    .url("https://port-0-kiosk-server-euegqv2blnemb8x8.sel5.cloudtype.app/order")
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
                ManageActivity.menuList = menuList;
                spreadOrderData(menuList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static int waitingCnt = -1;

    public void spreadOrderData(ArrayList<JSONObject> menuList) throws JSONException {
        LinearLayout allOrderView = findViewById(R.id.orderZone);
        allOrderView.removeAllViews();

        Typeface customFont = ResourcesCompat.getFont(this, R.font.gmarketsanslight);

        int curWaitingCnt = 0;
        for(int i=menuList.size()-1; i>=0; i--) { // 최신 주문이 위로
            String status = menuList.get(i).getString("status");
            int statusInt = Integer.parseInt(status);

            // 대기가 몇개인지 센다.
            if (statusInt == 0) {
                curWaitingCnt += 1;
            }

            if (orderViewSelect != 2) {
                if (statusInt != orderViewSelect) {
                    continue;
                }
            }

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
            tableText.setPadding(100, 14, 0, 14);
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
            nameText.setPadding(0, 14, 70, 14);
            nameText.setTextSize(23);
            nameText.setTextColor(Color.WHITE);
            nameText.setTypeface(customFont);
            nameText.setText(menuList.get(i).getJSONObject("menu").getString("name"));
            nameText.setGravity(Gravity.CENTER);
            textLayout.addView(nameText);

            // 주문 시간
            TextView timeText = new TextView(this);
            timeText.setLayoutParams(new LinearLayout.LayoutParams(
                    0, // 가로 가중치를 1로 설정하여 화면을 꽉 채우도록 함
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1 // 가로 가중치를 1로 설정하여 화면을 꽉 채우도록 함
            ));
            timeText.setPadding(60, 14, 0, 14);
            timeText.setTextSize(20);
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

            float desiredAlpha = 0.2f;
            if(status.equals("1") || status.equals(("-1"))){
                completeButton.setAlpha(desiredAlpha); // 불투명하게
                completeButton.setEnabled(false); // 클릭 안되게
                cancelButton.setAlpha(desiredAlpha); // 불투명하게
                cancelButton.setEnabled(false); // 클릭 안되게
            }
            newLayout.addView(textLayout);
            allOrderView.addView(newLayout);
        }

        Log.v("Need Code", "last: " + waitingCnt + " | cur: "+ curWaitingCnt);
        // 이전 대기 개수와 비교하기
        if (waitingCnt < 0) { // 한번도 초기화 안된 상태라면
            waitingCnt = curWaitingCnt;
        } else if (waitingCnt < curWaitingCnt) { // 대기 개수가 늘어났다면
            // 띵동~ 소리내기
            // 여기에 소리내는 코드를 추가해야함.
            playNewOrderSound();
            waitingCnt = curWaitingCnt;
            Log.v("Need Code", "띵동~");
        } else {
            waitingCnt = curWaitingCnt;
        }
    }
    // 소리나는 코드
    private void playNewOrderSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bell);
        }
        mediaPlayer.start();
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
                "https://port-0-kiosk-server-euegqv2blnemb8x8.sel5.cloudtype.app/order/changeStatus",
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
