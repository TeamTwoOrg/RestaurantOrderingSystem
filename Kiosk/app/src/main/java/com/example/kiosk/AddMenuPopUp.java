package com.example.kiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddMenuPopUp extends AppCompatActivity {

    private static final String BASE_URL = "https://sm-kiosk.kro.kr/menu/add";

    EditText menuNameEditText;
    EditText menuPriceEditText;
    EditText scriptEditText;
    Spinner category1Spinner;

    Button registerButton;
    List<String> itemList;
    ImageButton addImageButton;
    String imageURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_menu);

        addImageButton = findViewById(R.id.empty_plate);
        addImageButton.setImageResource(R.drawable.empty_plate);

        menuNameEditText = findViewById(R.id.menu_name);
        menuPriceEditText = findViewById(R.id.menu_price);
        scriptEditText = findViewById(R.id.script);
        category1Spinner = findViewById(R.id.category_1_spinner);

        registerButton = findViewById(R.id.register_button);

        itemList = new ArrayList<>();
        itemList.add("안주");
        itemList.add("주류");
        itemList.add("음료");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMenuPopUp.this, R.layout.spinner_list_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category1Spinner.setAdapter(adapter);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imageIntent, 1);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setBackgroundResource(R.drawable.button_background_clicked); // 변경 코드를 먼저 실행
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        registerButton.setBackgroundResource(R.drawable.button_back);
                    }
                }, 300);
                Toast.makeText(AddMenuPopUp.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                String id = LoginActivity.cur_id;
                String password = LoginActivity.cur_pw;
                String name = menuNameEditText.getText().toString();
                String text = scriptEditText.getText().toString();
                int price = Integer.parseInt(menuPriceEditText.getText().toString());
                String selectedCategory1 = category1Spinner.getSelectedItem().toString();

                String encodedImageURL = "";
                try {
                    encodedImageURL = URLEncoder.encode(imageURL, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("URL_ENCODE_ERROR", "URL encoding error: " + e.getMessage());
                }

                addToServer(id, password, name, text, price, selectedCategory1, encodedImageURL);
            }
        });
    }

    public void addToServer(String id, String password, String name, String text, int price, String selectedCategory1, String encodedImageURL) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);
            jsonBody.put("password", password);
            jsonBody.put("name", name);
            jsonBody.put("text", text);
            jsonBody.put("price", price);
            jsonBody.put("imageURL", encodedImageURL);
            jsonBody.put("category1", selectedCategory1);

            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    e.printStackTrace();
                    // Handle failure
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Log.d("POST", "Response: " + responseBody);
                        // Handle success
                    } else {
                        Log.e("POST", "Request failed. Code: " + response.code());
                        // Handle failure
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();

                addImageButton.setImageBitmap(img);
                imageURL = data.getData().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
