package com.teamtwo.restaurantorderingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import com.bumptech.glide.Glide;

public class MenuForUser extends AppCompatActivity {
    ImageButton voiceImageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_for_menu);
        voiceImageButton = findViewById(R.id.voiceIcon);

        Glide.with(this).load(R.drawable.white_voice).into(voiceImageButton);
    }
}