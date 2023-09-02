package com.example.kiosk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.util.Log;


import com.teamtwo.kiosk.R;

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

    public static String cur_id = "";
    public static String cur_pw = "";
    EditText edt1, edt2;
    Button btn1;
    // 약간의 불투명도를 설정합니다 (예: 0.7)
    float originAlpha = 1.0f;
    float desiredAlpha = 0.2f;

    // 관리자 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        Button manage_button = (Button) findViewById(R.id.manage_button); // 관리자 버튼
        Button user_button = (Button) findViewById(R.id.user_button); // 사용자 버튼
        Button logout_button = (Button) findViewById(R.id.logout_button);
        Button login_button = (Button) findViewById(R.id.login_button);

        manage_button.setAlpha(desiredAlpha);
        manage_button.setEnabled(false);
        user_button.setAlpha(desiredAlpha);
        user_button.setEnabled(false);
        logout_button.setAlpha(desiredAlpha);
        logout_button.setEnabled(false);

        login_button.setAlpha(originAlpha);
        login_button.setEnabled(true);

        EditText id_text = findViewById(R.id.id_text);
        EditText password_text = findViewById(R.id.password_text);
        id_text.setText("");
        password_text.setText("");
        id_text.setAlpha(originAlpha);
        id_text.setEnabled(true);
        password_text.setAlpha(originAlpha);
        password_text.setEnabled(true);


        // 관리자버튼 작동
        manage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manage_button.setBackgroundResource(R.drawable.login_button_background);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        manage_button.setBackgroundResource(R.drawable.button_round);

                    }
                }, 200);

                Intent intent = new Intent(getApplicationContext(), ManageActivity.class);
                startActivity(intent);
            }
        });

        // 로그아웃 버튼 작동
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_button.setBackgroundResource(R.drawable.login_button_background);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logout_button.setBackgroundResource(R.drawable.button_round);

                    }
                }, 200);

                Button manage_button = (Button) findViewById(R.id.manage_button); // 관리자 버튼
                Button user_button = (Button) findViewById(R.id.user_button); // 사용자 버튼
                Button logout_button = (Button) findViewById(R.id.logout_button);
                Button login_button = (Button) findViewById(R.id.login_button);

                manage_button.setAlpha(desiredAlpha);
                manage_button.setEnabled(false);
                user_button.setAlpha(desiredAlpha);
                user_button.setEnabled(false);
                logout_button.setAlpha(desiredAlpha);
                logout_button.setEnabled(false);

                login_button.setAlpha(originAlpha);
                login_button.setEnabled(true);

                EditText id_text = findViewById(R.id.id_text);
                EditText password_text = findViewById(R.id.password_text);
                id_text.setText("");
                password_text.setText("");
                id_text.setAlpha(originAlpha);
                id_text.setEnabled(true);
                password_text.setAlpha(originAlpha);
                password_text.setEnabled(true);
            }
        });




        // 로그인 버튼 작동
        edt1 = findViewById(R.id.id_text);
        edt2 = findViewById(R.id.password_text);
        btn1 = findViewById(R.id.login_button);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                login_button.setBackgroundResource(R.drawable.login_button_background);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        login_button.setBackgroundResource(R.drawable.button_round);

                    }
                }, 200);

                String name = edt1.getText().toString();
                String password = edt2.getText().toString();

                // 로그로 아이디와 비밀번호를 출력 (디버그용)
                Log.d("LoginActivity", "아이디: " + name);
                Log.d("LoginActivity", "비밀번호: " + password);

                cur_id = name;
                cur_pw = password;

                // 서버에 로그인 정보 전송
                loginToServer(name, password);

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

                            Button manage_button = (Button) findViewById(R.id.manage_button); // 관리자 버튼
                            Button user_button = (Button) findViewById(R.id.user_button); // 사용자 버튼
                            Button logout_button = (Button) findViewById(R.id.logout_button);
                            Button login_button = (Button) findViewById(R.id.login_button);

                            manage_button.setAlpha(desiredAlpha);
                            manage_button.setEnabled(false);
                            user_button.setAlpha(desiredAlpha);
                            user_button.setEnabled(false);
                            logout_button.setAlpha(desiredAlpha);
                            logout_button.setEnabled(false);

                            login_button.setAlpha(originAlpha);
                            login_button.setEnabled(true);

                            EditText id_text = findViewById(R.id.id_text);
                            EditText password_text = findViewById(R.id.password_text);
                            id_text.setText("");
                            password_text.setText("");
                            id_text.setAlpha(originAlpha);
                            id_text.setEnabled(true);
                            password_text.setAlpha(originAlpha);
                            password_text.setEnabled(true);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Button manage_button = (Button) findViewById(R.id.manage_button);
                    Button user_button = (Button) findViewById(R.id.user_button);
                    Button login_button = (Button) findViewById(R.id.login_button);
                    Button logout_button = (Button) findViewById(R.id.logout_button);

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

                                        Button manage_button = (Button) findViewById(R.id.manage_button); // 관리자 버튼
                                        Button user_button = (Button) findViewById(R.id.user_button); // 사용자 버튼
                                        Button logout_button = (Button) findViewById(R.id.logout_button);
                                        Button login_button = (Button) findViewById(R.id.login_button);

                                        manage_button.setAlpha(originAlpha);
                                        manage_button.setEnabled(true);
                                        user_button.setAlpha(originAlpha);
                                        user_button.setEnabled(true);
                                        logout_button.setAlpha(originAlpha);
                                        logout_button.setEnabled(true);

                                        login_button.setAlpha(desiredAlpha);
                                        login_button.setEnabled(false);

                                        id_text.setText("");
                                        password_text.setText("");
                                        id_text.setAlpha(desiredAlpha);
                                        id_text.setEnabled(false);
                                        password_text.setAlpha(desiredAlpha);
                                        password_text.setEnabled(false);
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

                                        Button manage_button = (Button) findViewById(R.id.manage_button); // 관리자 버튼
                                        Button user_button = (Button) findViewById(R.id.user_button); // 사용자 버튼
                                        Button logout_button = (Button) findViewById(R.id.logout_button);
                                        Button login_button = (Button) findViewById(R.id.login_button);

                                        manage_button.setAlpha(originAlpha);
                                        manage_button.setEnabled(true);
                                        user_button.setAlpha(originAlpha);
                                        user_button.setEnabled(true);
                                        logout_button.setAlpha(originAlpha);
                                        logout_button.setEnabled(true);

                                        login_button.setAlpha(desiredAlpha);
                                        login_button.setEnabled(false);

                                        EditText id_text = findViewById(R.id.id_text);
                                        EditText password_text = findViewById(R.id.password_text);
                                        id_text.setText("");
                                        password_text.setText("");
                                        id_text.setAlpha(originAlpha);
                                        id_text.setEnabled(true);
                                        password_text.setAlpha(originAlpha);
                                        password_text.setEnabled(true);

                                        // AlertDialog를 보여준다.
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            manage_button.setAlpha(desiredAlpha);
                            manage_button.setEnabled(false);
                            user_button.setAlpha(desiredAlpha);
                            user_button.setEnabled(false);
                            logout_button.setAlpha(desiredAlpha);
                            logout_button.setEnabled(false);

                            EditText id_text = findViewById(R.id.id_text);
                            EditText password_text = findViewById(R.id.password_text);
                            id_text.setText("");
                            password_text.setText("");
                            id_text.setAlpha(originAlpha);
                            id_text.setEnabled(true);
                            password_text.setAlpha(originAlpha);
                            password_text.setEnabled(true);
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

