package com.example.kiosk;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list); // 화면 구성 XML 파일을 지정


        // ListView를 레이아웃에서 찾아옴
        ListView listView = findViewById(R.id.productListView);




    }

}
