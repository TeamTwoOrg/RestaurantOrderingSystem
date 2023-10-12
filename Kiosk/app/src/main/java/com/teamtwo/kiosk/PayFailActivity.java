package com.teamtwo.kiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PayFailActivity extends AppCompatActivity {

    ImageView apologized_muji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fail);

        apologized_muji = findViewById(R.id.apologized_muji);
        Glide.with(this).load(R.drawable.sorry_we_cant).into(apologized_muji);
    }

    public void payEndConfirm(View view) {
        onBackPressed();
    }
}