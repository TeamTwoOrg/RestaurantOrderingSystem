package com.example.kiosk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity{

    private ListView productlist;
    private Button productAll_btn;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity); // manage.xml 레이아웃에 정의

        productlist = findViewById(R.id.productlist); // 안에 있는 ListView id가 list라고 지정.
        productAll_btn = findViewById(R.id.productAll_btn); // 상품전체 보기 버튼


        List<String> data = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_custom, data); // listView의 외관을 사용자 정의
        productlist.setAdapter(adapter);





        adapter.notifyDataSetChanged();


        };

}
