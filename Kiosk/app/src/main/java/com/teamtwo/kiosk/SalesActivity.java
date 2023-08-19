package com.teamtwo.kiosk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SalesActivity extends AppCompatActivity {

    Spinner yearSpinner;
    Spinner monthSpinner;
    Spinner daySpinner;
    List<String> itemList;
    List<String> itemList2;
    List<String> itemList3;
    Button salesSlipbtn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_activity);

        // 년도 스피너
        yearSpinner = findViewById(R.id.year_spinner);
        // 월 스피너
        monthSpinner = findViewById(R.id.month_spinner);
        // 일 스피너
        daySpinner = findViewById(R.id.day_spinner);
        // 그래프 버튼
        salesSlipbtn = findViewById(R.id.salesSlipbtn);

        itemList = new ArrayList<>();
        itemList2 = new ArrayList<>();
        itemList3 = new ArrayList<>();


        // 년도 스피너
        int startYear = 2000; // 시작 년도
        int numberOfYears = 100; // 100년까지

        for (int i = 0; i < numberOfYears; i++) {
            itemList.add(String.valueOf(startYear + i));
        }

        // 월 스피너
        int startMonth = 1;
        int numberOfMonths = 12;

        for (int i = 0; i < numberOfMonths; i++) {
            itemList2.add(String.valueOf(startMonth + i));
        }

        // 일 스피너
        int startDay = 1;
        int numberOfDay = 31;

        //itemList3.add(String.valueOf("전체"));
        for (int i = 0; i < numberOfDay; i++) {
            itemList3.add(String.valueOf(startDay + i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SalesActivity.this, R.layout.spinner_list_item, itemList);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(SalesActivity.this, R.layout.spinner_list_item, itemList2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(SalesActivity.this, R.layout.spinner_list_item, itemList3);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);
        monthSpinner.setAdapter(adapter2);
        daySpinner.setAdapter(adapter3);

        // 오늘 날짜 초기값 설정
        Calendar calendar = Calendar.getInstance(); // 캘린더 객체 생성하여 현재 시간 가져오기
        int curYear = calendar.get(Calendar.YEAR); // 현재 년도 가져오기
        int curMonth = calendar.get(Calendar.MONTH) + 1; // 현재 월 가져오기 (+1 더해주는 이유는 Calendar.MONTH가 0~11 값을 반환하기 때문입니다.)
        int curDay = calendar.get(Calendar.DAY_OF_MONTH) + 1; // 현재 일자 가져오기

        // 현재 날짜에 해당하는 항목을 선택하도록 설정
        yearSpinner.setSelection(itemList.indexOf(String.valueOf(curYear)));
        monthSpinner.setSelection(itemList2.indexOf(String.valueOf(curMonth)));
        daySpinner.setSelection(itemList3.indexOf(String.valueOf(curDay)));

        getSales(curYear, curMonth, curDay);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); // 선택된 항목 가져오기
                Log.d("Spinner", "Selected item: " + selectedItem); // 로그에 출력

                // 선택된 값을 int 형으로 변환하기
                int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
                int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());
                int day = Integer.parseInt(daySpinner.getSelectedItem().toString());

                // getSales 함수 호출하기
                getSales(year, month, day); // year, month, day를 매개변수로 전달
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); // 선택된 항목 가져오기
                Log.d("Spinner", "Selected item: " + selectedItem); // 로그에 출력

                // 선택된 값을 int 형으로 변환하기
                int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
                int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());
                int day = Integer.parseInt(daySpinner.getSelectedItem().toString());

                // getSales 함수 호출하기
                getSales(year, month, day); // year, month, day를 매개변수로 전달
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); // 선택된 항목 가져오기
                Log.d("Spinner", "Selected item: " + selectedItem); // 로그에 출력

                // 선택된 값을 int 형으로 변환하기
                int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
                int month = Integer.parseInt(monthSpinner.getSelectedItem().toString());
                int day = Integer.parseInt(daySpinner.getSelectedItem().toString());

                // getSales 함수 호출하기
                getSales(year, month, day); // year, month, day를 매개변수로 전달
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 매출통계
        salesSlipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "매출 통계" 버튼이 클릭되었을 때 동작
                // 상품 목록 화면을 표시하는 로직을 추가
                // 예시: Intent로 다음 화면으로 이동
                salesSlipbtn.setBackgroundResource(R.drawable.darker_button_half_diff_background);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        salesSlipbtn.setBackgroundResource(R.drawable.button_round_half);

                    }
                }, 200);

                Intent intent = new Intent(SalesActivity.this, SalesSlipActivity.class);
                startActivity(intent);

            }
        });
    }

    public void getSales(int year, int month, int day) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", LoginActivity.cur_id);
            jsonBody.put("password", LoginActivity.cur_pw);
            jsonBody.put("year", year);
            jsonBody.put("month", month);
            jsonBody.put("day", day);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerCommunicationHelper.sendRequest(
                "https://sm-kiosk.kro.kr/order/dayOrder",
                ServerCommunicationHelper.HttpMethod.POST,
                jsonBody,
                new ServerCommunicationHelper.ResultCallback() {
                    @Override
                    public void onSuccess(String responseBody) {
                        try {
                            // 받아온 JSON 데이터를 파싱
                            JSONObject responseJson = new JSONObject(responseBody);
                            JSONObject data = responseJson.getJSONObject("data");
                            JSONObject count = data.getJSONObject("count");
                            int totalPrice = data.getInt("totalPrice");


                            // JSONObject에서 키들을 Iterator로 가져오기
                            Iterator<String> keysIterator = count.keys();
                            LinearLayout salesLayout = findViewById(R.id.sales); // sales LinearLayout
                            LinearLayout totalLayout = findViewById(R.id.totalprice); // total LinearLayout
                            salesLayout.removeAllViews();// 하나만 나와야하는데 계속나오는거 수정
                            totalLayout.removeAllViews();// 하나만 나와야하는데 계속나오는거 수정.

                            // 모든 키와 값 출력하기
                            while (keysIterator.hasNext()) {
                                String menu = keysIterator.next(); // 메뉴
                                String value = count.getString(menu); // 값

                                // 데이터를 활용하여 화면에 표시하는 로직을 작성
                                displaySalesData(menu, value);
                            }

                            displaytotal(totalPrice);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("xxx");
                    }
                }
        );
    }

    private void displaytotal(int totalPrice) {
        LinearLayout totalLayout = findViewById(R.id.totalprice); // total LinearLayout

        // TextView를 생성하고 데이터를 설정하여 추가
        TextView menuTextView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(115, 0, 0, 0); // 마진 추가 (위, 왼쪽, 아래, 오른쪽)
        layoutParams.gravity = Gravity.CENTER; // TextView를 가운데로 정렬하기 위해 gravity 설정
        menuTextView.setLayoutParams(layoutParams);
        menuTextView.setTextSize(40);
        menuTextView.setTextColor(Color.WHITE);
        menuTextView.setText("총 매출 : " + totalPrice + "원");

        totalLayout.addView(menuTextView);
    }

    private void displaySalesData(String menuName, String menuCount) {
        LinearLayout salesLayout = findViewById(R.id.sales); // sales LinearLayout

        // TextView를 생성하고 데이터를 설정하여 추가
        TextView menuTextView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(115, 30, 0, 30); // 마진 추가 (위, 왼쪽, 아래, 오른쪽)
        menuTextView.setLayoutParams(layoutParams);
        menuTextView.setTextSize(25);
        menuTextView.setTextColor(Color.WHITE);
        menuTextView.setText(menuName + ": " + menuCount + "개");
        salesLayout.addView(menuTextView);
    }
}