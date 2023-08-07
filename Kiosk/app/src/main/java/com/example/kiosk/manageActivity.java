package com.example.kiosk;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;

public class manageActivity extends AppCompatActivity {

    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage); // 보여줄 화면 설정

        list = (ListView)findViewById(R.id.list);

        // 배열 안에다가 string형태로 list뷰에 넣는다.
        List<String> data = new ArrayList<>();
        // 리스트뷰랑 리스트를 연결하는 adapter 생성
        // simple_list_item_1을 기본으로 주어지는 listview 디자인
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        // list와 adapter 연결
        list.setAdapter(adapter);
        
        data.add("주문완료");
        data.add("주문대기");
        data.add("주문취소");
        // 이 상태로 저장을 하겠다.
        adapter.notifyDataSetChanged(); 

    }
}
