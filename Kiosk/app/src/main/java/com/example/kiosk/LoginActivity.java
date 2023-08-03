package com.example.kiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://172.20.10.2:4000/user/login";
    EditText edt1, edt2;
    Button btn1;
    Button manage_button;
    // 관리자 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        Button manage_button = (Button) findViewById(R.id.manage_button); // 버튼아이디
        manage_button.setEnabled(false); // 로그인 전까지 관리자 버튼 비활성화



        manage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), manageActivity.class);
                startActivity(intent);
            }
        });



        // 로그인
        edt1 = findViewById(R.id.id_text);
        edt2 = findViewById(R.id.password_text);
        btn1 = findViewById(R.id.login_button);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String name = edt1.getText().toString();
                String password = edt2.getText().toString();

                // 로그로 아이디와 비밀번호를 출력 (디버그용)
                Log.d("LoginActivity", "아이디: " + name);
                Log.d("LoginActivity", "비밀번호: " + password);

                // 서버에 로그인 정보 전송
                loginToServer(name, password);

                Intent intent = new Intent(getApplicationContext(), manageActivity.class);
                startActivity(intent);
            }
        });
    }
    public void loginToServer(String username, String password) {

        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", username);
            jsonBody.put("password", password);
            String requestBody = jsonBody.toString();
            System.out.println(requestBody);

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody formBody = RequestBody.create(requestBody, JSON);

            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(formBody)
                    .build();

            System.out.println("dd");
//            if(a==1) {
//                return;
//            }


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("fail");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Button manage_button = (Button) findViewById(R.id.manage_button);


                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            boolean ok = jsonObject.getBoolean("ok");
                            if (ok) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("login success");


                                        manage_button.setEnabled(true); // 관리자 버튼 활성화


                                        // 로그인 성공 시 다음 화면으로 이동하거나 액션을 취할 수 있습니다.
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("login fail");
                                    }

                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("fail");
                            }
                        });
                    }
                }
            });


        } catch(JSONException e) {
            e.printStackTrace();
        }

    }
}

