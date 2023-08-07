package com.teamtwo.restaurantorderingsyste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class picturechoicepopup extends AppCompatActivity {

    private TextView textView3;
    private int currentValue = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picturechoicepopup);

        textView3 = findViewById(R.id.textView3);

        ImageButton minusButton = findViewById(R.id.imageButton2);
        ImageButton plusButton = findViewById(R.id.imageButton4);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentValue > 1) {
                    currentValue--;
                    textView3.setText(String.valueOf(currentValue));
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentValue++;
                textView3.setText(String.valueOf(currentValue));
            }
        });
    }
}
