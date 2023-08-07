package com.teamtwo.restaurantorderingsyste;
//사용자 초기화면 ui
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.teamtwo.restaurantorderingsyste.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startscreen);


        ImageButton myButton = findViewById(R.id.imageButton);
        myButton.setOnClickListener(v -> {
            // 새로운 액티비티로 이동
            //Intent intent = new Intent(MainActivity.this, .class);
            //startActivity(intent);
        });

    }
}
