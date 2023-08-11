package com.example.kiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class AddMenuPopUp extends AppCompatActivity {
    Spinner spinner1;
    List<String> itemList;
    ImageButton addImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_menu);
        addImageButton = (ImageButton) findViewById(R.id.empty_plate);
        addImageButton.setImageResource(R.drawable.empty_plate);

        spinner1 = findViewById(R.id.category_1_spinner);
        itemList = new ArrayList<>();
        itemList.add("안주");
        itemList.add("주류");
        itemList.add("음료");

        // 스피너 아이템 선택 시 이벤트 처리
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemList.get(position);
                // 여기에서 선택된 아이템에 대한 처리를 수행
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때 처리할 내용을 여기에 작성
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMenuPopUp.this, R.layout.spinner_list_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        Button registerButton = findViewById(R.id.register_button);
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
                Toast.makeText(AddMenuPopUp.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                // 클릭 시 버튼의 배경을 변경합니다.
                registerButton.setBackgroundResource(R.drawable.button_background_clicked);

                // 클릭 이벤트 동작
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        registerButton.setBackgroundResource(R.drawable.button_background);
                    }
                }, 100);

                String id = "your_id";
                String password = "your_password";
                String name = "your_name";
                String text = "your_text";
                int price = 1000;
                String imageURL = "your_image_url";
                String category1 = "your_category1";

                // OkHttp 클라이언트 생성
                OkHttpClient client = new OkHttpClient();

                // POST 요청 바다 생성
                //RequestBody requestBody = new FormBody.Builder()

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();


                    addImageButton.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}