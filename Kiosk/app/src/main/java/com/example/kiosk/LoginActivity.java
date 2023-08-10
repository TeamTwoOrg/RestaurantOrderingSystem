package com.example.kiosk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    private static final String BASE_URL = "https://sm-kiosk.kro.kr/user/login";
    public static String password_text;
    public static String id_text;
    EditText edt1, edt2;
    Button btn1;
    Button manage_button;
    // 관리자 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        Button manage_button = (Button) findViewById(R.id.manage_button); // 관리자 버튼
        Button user_button = (Button) findViewById(R.id.user_button); // 사용자 버튼
        Button login_button = (Button) findViewById(R.id.login_button); // 로그인 버튼

        manage_button.setVisibility(View.INVISIBLE); // 로그인 전까지 관리자 버튼 비활성화
        user_button.setVisibility(View.INVISIBLE); // 로그인 전까지 사용자 버튼 비활성화

        // 관리자버튼 작동
        manage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageActivity.class);
                startActivity(intent);
            }
        });

        Button logout_button = (Button) findViewById(R.id.logout_button); // 관리자 버튼

        // 로그아웃 버튼 작동
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manage_button.setVisibility(View.INVISIBLE); // 로그인 전까지 관리자 버튼 비활성화
                user_button.setVisibility(View.INVISIBLE); // 로그인 전까지 사용자 버튼 비활성화
                login_button.setVisibility(View.VISIBLE); // 로그인 버튼 활성화
            }
        });




        // 로그인
        edt1 = findViewById(R.id.id_text);
        edt2 = findViewById(R.id.password_text);
        btn1 = findViewById(R.id.login_button);

        id_text = String.valueOf(edt1.getText());
        password_text = String.valueOf(edt2.getText());



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

//                // 임시 관리자 페이지 넘어가는 버튼
//                Intent intent = new Intent(getApplicationContext(), ManageActivity.class);
//                startActivity(intent);
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
                            // AlertDialog 설정
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("로그인 실패");
                            builder.setMessage("로그인 실패 했습니다.");

                            // 확인 버튼을 설정하고 클릭시 닫는다.
                            builder.setPositiveButton("확인", null);

                            // AlertDialog를 보여준다.
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Button manage_button = (Button) findViewById(R.id.manage_button);
                    Button user_button = (Button) findViewById(R.id.user_button);
                    Button login_button = (Button) findViewById(R.id.login_button);

                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            boolean ok = jsonObject.getBoolean("ok");
                            if (ok) {
                                // 로그인 성공 시 다음 화면으로 이동하거나 액션을 취할 수 있습니다.
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 로그인 성공 디버그 확인용

                                        EditText id_text = findViewById(R.id.id_text);
                                        EditText password_text = findViewById(R.id.password_text);
                                        System.out.println("login success");

                                        // AlertDialog 설정
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setTitle("로그인 성공");
                                        builder.setMessage("로그인에 성공하셨습니다.");

                                        id_text.setText("");
                                        password_text.setText("");

                                        // 확인 버튼을 설정하고 클릭시 닫는다.
                                        builder.setPositiveButton("확인", null);

                                        // AlertDialog를 보여준다.
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                        // 로그인 성공시 로그인버튼 비활성화
                                        login_button.setVisibility(View.INVISIBLE);

                                        // 로그인 성공시 보여주는 버튼
                                        manage_button.setVisibility(View.VISIBLE); // 관리자 버튼 활성화
                                        user_button.setVisibility(View.VISIBLE); // 사용자 버튼 활성화

//                                        // 로그인 성공 후에만 manageActivity로 이동
//                                        Intent intent = new Intent(getApplicationContext(), ManageActivity.class);
//                                        startActivity(intent);
                                    }

                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("login fail");
                                        // AlertDialog 설정
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setTitle("로그인 실패");
                                        builder.setMessage("로그인 실패 했습니다.");

                                        // 확인 버튼을 설정하고 클릭시 닫는다.
                                        builder.setPositiveButton("확인", null);

                                        // AlertDialog를 보여준다.
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
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
                                // AlertDialog 설정
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("로그인 실패");
                                builder.setMessage("로그인 실패 했습니다.");

                                // 확인 버튼을 설정하고 클릭시 닫는다.
                                builder.setPositiveButton("확인", null);

                                // AlertDialog를 보여준다.
                                AlertDialog dialog = builder.create();
                                dialog.show();
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

