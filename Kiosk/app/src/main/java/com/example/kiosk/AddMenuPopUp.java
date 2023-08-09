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
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import java.io.InputStream;

public class AddMenuPopUp extends AppCompatActivity {
    Spinner spinner1;
    List<String> itemList;
    ImageButton addImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_menu);
        addImageButton = (ImageButton)findViewById(R.id.empty_plate);
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

        addImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imageIntent, 1);

            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
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