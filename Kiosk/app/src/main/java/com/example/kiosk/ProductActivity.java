package com.example.kiosk;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity{

    private static final String BASE_URL = "https://sm-kiosk.kro.kr/user/login";
    private Button productAll_btn;
    private Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        productAll_btn = findViewById(R.id.productAll_btn);
        addButton = findViewById(R.id.addButton);




        productAll_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // "상품 목록" 버튼이 클릭되었을 때 동작
                // 상품 목록 화면을 표시하는 로직을 추가
                // 예시: Intent로 다음 화면으로 이동
                Intent intent = new Intent(ProductActivity.this, ProductListActivity.class);
                startActivity(intent);
                Toast.makeText(ProductActivity.this, "버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
                //fetchMenuListFromMongoDB();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // "+" 버튼이 클릭되었을 때 동작
                // 상품을 추가하는 로직을 추가
                // 예시: Toast 메시지를 표시
                Intent intent = new Intent(ProductActivity.this, AddMenuPopUp.class);
                startActivity(intent);
                Toast.makeText(ProductActivity.this, "상품이 추가 되었습니다.", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void fetchMenuListFromMongoDB() {
        OkHttpClient client = new OkHttpClient();

        // POST 요청에 필요한 JSON 데이터 생성
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", "b");
            jsonBody.put("password", "1234");

            // 필요한 다른 데이터도 추가
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // POST 요청 생성
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("https://sm-kiosk.kro.kr/menu")  // 요청 URL 설정
                .post(requestBody)
                .build();

        // 요청 보내고 응답 처리
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {

                String responseBody = response.body().string();

                // JSON 파싱
                JSONArray jsonArray = new JSONArray(responseBody);

                // 받아온 메뉴 정보를 활용하여 처리 (예시로 로그 출력)
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject menuObject = jsonArray.getJSONObject(i);
                    String menu_name = menuObject.getString("menu_name");
                    double menu_price = menuObject.getDouble("menu_price");
                    String imageURL = menuObject.getString("imageURL");

                    Log.d("TAG", "메뉴 이름: " + menu_name);
                    Log.d("TAG", "메뉴 가격: " + menu_price);
                    Log.d("TAG", "이미지 URL: " + imageURL);
                    // 여기에서 받아온 정보를 활용하여 UI에 표시하거나 다른 처리를 수행
                    // 예를 들어, 리스트뷰에 추가하거나 뷰를 동적으로 생성할 수 있습니다.
                }
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(ProductActivity.this, "메뉴 리스트를 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}