package com.example.kiosk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity{

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
                // Intent intent = new Intent(ProductActivity.this, ProductListActivity.class);
                // startActivity(intent);
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
}