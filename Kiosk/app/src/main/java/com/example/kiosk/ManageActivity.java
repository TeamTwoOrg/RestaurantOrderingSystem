package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ManageActivity extends AppCompatActivity {

    private ListView Allorderlist;
    private Button productBtn;
    private Button orderBtn;
    private Button waitingBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage); // manage.xml 레이아웃에 정의

        Allorderlist = findViewById(R.id.Allorderlist); // 안에 있는 ListView id가 list라고 지정.
        productBtn = findViewById(R.id.product_btn); // 상품보기 버튼
        orderBtn = findViewById(R.id.order_btn); // 주문보기 버튼
        waitingBtn = findViewById(R.id.waiting_btn); // 웨이팅보기 버튼

        List<String> data = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_custom, data); // listView의 외관을 사용자 정의
        Allorderlist.setAdapter(adapter);

        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");
        data.add("  " + "주문완료" + "           " + "16" + "             " + "바게트" + "       " + "12:44:33");



        adapter.notifyDataSetChanged();

        // 상품보기 버튼 클릭 리스너
        productBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // productActivity로 전환하기 위한 intent 생성
                Intent intent = new Intent(ManageActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
    }
}
