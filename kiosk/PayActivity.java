package com.teamtwo.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayActivity extends AppCompatActivity {

    // Request 작업을 할 Queue
    static RequestQueue requestQueue;

    // 결제 정보를 받을 변수
    static String productName; // 상품 이름
    static String productPrice; // 상품 가격

    // 웹 뷰
    WebView webView;

    // json 파싱
    Gson gson;

    // 커스텀 웹 뷰 클라이언트
    MyWebViewClient myWebViewClient;

    // 결제 고유 번호
    String tidPin;

    // 결제 요청 토큰
    String pgToken;
    String baseURL;

    // 기본 생성자
    // - Activity는 기본 생성자가 없으면 Manifest에서 사용하지 못함.
    // - 만약 생성자를 오버라이딩 했다면 기본 생성자를 작성해 둘것!
    public PayActivity() {

    }

    // 상품 이름과 가격을 초기화할 생성자
    public PayActivity(String productName, String productPrice) {
        PayActivity.productName = productName;
        PayActivity.productPrice = productPrice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

//        baseURL = "http://192.168.3.49:4000";
        baseURL = "https://sm-kiosk.kro.kr";
        // 초기화
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        myWebViewClient = new MyWebViewClient();
        webView = findViewById(R.id.webView);
        gson = new Gson();

        // 웹 뷰 설정
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(myWebViewClient);

        // 실행 시 바로 결제 Http 통신 실행
        requestQueue.add(myWebViewClient.readyRequest);
    }

    public class MyWebViewClient extends WebViewClient {

        // 에러 - 통신을 받을 Response 변수
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Debug", "Error : " + error);
            }
        };

        // 결제 준비 단계 - 통신을 받을 Response 변수
        Response.Listener<String> readyResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Debug", response);
                // 결제가 성공 했다면 돌려받는 JSON객체를 파싱함.
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response);

                // get("받을 Key")로 Json 데이터를 받음
                // - 결제 요청에 필요한 next_redirect_mobile_url, tid를 파싱
                String url = element.getAsJsonObject().get("url").getAsString();
                String tid = element.getAsJsonObject().get("tid").getAsString();
                Log.d("Debug", "url : " + url);
                Log.d("Debug", "tid : " + tid);

                webView.loadUrl(url);
                tidPin = tid;
            }
        };

        // 결제 준비 단계 - 통신을 넘겨줄 Request 변수
        StringRequest readyRequest = new StringRequest(Request.Method.POST, baseURL+"/pay/ready", readyResponse, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("Debug", "name : " + productName);
                Log.d("Debug", "price : " + productPrice);

                Map<String, String> params = new HashMap<>();
                params.put("item_name", productName); // 상품 이름
                params.put("total_amount", productPrice); // 상품 총액
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };

        // 결제 요청 단계 - 통신을 받을 Response 변수
        Response.Listener<String> approvalResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Debug", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean ok = jsonObject.getBoolean("ok");

                    if (ok) {
                        // 'ok' 값이 true인 경우 처리
                        Log.d("Debug", "JSON 응답의 'ok' 값이 true입니다.");
                        webView.loadUrl(baseURL+"/pay/finalSuccess");
                        onBackPressed();
                        // PaySuccessActivity를 실행하는 Intent 생성
                        Intent intent = new Intent(getApplicationContext(), PaySuccessActivity.class);

                        // PaySuccessActivity 실행
                        startActivity(intent);
                    } else {
                        // 'ok' 값이 false인 경우 처리
                        Log.d("Debug", "JSON 응답의 'ok' 값이 false입니다.");
                        webView.loadUrl(baseURL+"/pay/finalFail");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Debug", "JSON 파싱 에러: " + e.getMessage());
                }
            }
        };

        // 결제 요청 단계 - 통신을 넘겨줄 Request 변수
        StringRequest approvalRequest = new StringRequest(Request.Method.POST, baseURL+"/pay/approve", approvalResponse, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tid", tidPin);
                params.put("pg_token", pgToken);
                params.put("total_amount", productPrice);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };

        // URL 변경시 발생 이벤트
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String[] splited = url.split("/");
            String urlFinal = splited[splited.length - 1];
            if (urlFinal.equals("cancel") || urlFinal.equals("fail")) {
                onBackPressed();
                Intent intent = new Intent(getApplicationContext(), PayFailActivity.class);
                startActivity(intent);
                return false;
            }

            Log.e("Debug", "url" + url);
            if (url != null && url.contains("pg_token=")) {
                String pg_Token = url.substring(url.indexOf("pg_token=") + 9);
                pgToken = pg_Token;

                requestQueue.add(approvalRequest);

            } else if (url != null && url.startsWith("intent://")) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                    if (existPackage != null) {
                        startActivity(intent);
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            view.loadUrl(url);
            return false;
        }
    }
}