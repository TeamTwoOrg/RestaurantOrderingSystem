package com.teamtwo.kiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.bumptech.glide.Glide;
import com.teamtwo.kiosk.fragments.BeverageFragment;
import com.teamtwo.kiosk.fragments.LiquorFragment;
import com.teamtwo.kiosk.fragments.MealFragment;
import com.teamtwo.kiosk.fragments.SnackFragment;


import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MenuForUser extends AppCompatActivity {
    private Button cartButton;
    private Button voice_recognition_btn_text;
    private ImageButton voice_recognition_btn;
    private FragmentManager fragmentManager;

    private FragmentTransaction transaction;

    private BeverageFragment fragmentBeverage;
    private LiquorFragment fragmentLiquor;
    private SnackFragment fragmentSnack;
    private MealFragment fragmentMeal;

    final int PERMISSION = 1;

    private String name;

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_for_menu);

        // 안드로이드 6.0버전 이상인지 체크해서 퍼미션 체크
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        //음성 인식 버튼
        voice_recognition_btn_text = findViewById(R.id.voice_recognition_btn_text);

        //음성 인식 gif
        voice_recognition_btn = findViewById(R.id.voice_recognition_btn);

        cartButton = findViewById(R.id.cartbutton);

        Glide.with(this).load(R.drawable.white_voice).into(voice_recognition_btn);

        //Fragment 영역
        fragmentManager = getSupportFragmentManager();

        fragmentBeverage = new BeverageFragment();
        fragmentLiquor = new LiquorFragment();
        fragmentSnack = new SnackFragment();
        fragmentMeal = new MealFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.switching_zone, fragmentSnack).commitAllowingStateLoss();

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCartPopup();
            }
        });

        voice_recognition_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showCustomVoiceDialog();}
        });

        voice_recognition_btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showCustomVoiceDialog();}
        });
    }



    public void clickHandler(View view)
    {
        transaction = fragmentManager.beginTransaction();

        int id = view.getId();
        if (id == R.id.topButtonSnack) {
            transaction.replace(R.id.switching_zone, fragmentSnack).commitAllowingStateLoss();
        } else if (id == R.id.topButtonLiquor) {
            transaction.replace(R.id.switching_zone, fragmentLiquor).commitAllowingStateLoss();
        } else if (id == R.id.topButtonBeverage) {
            transaction.replace(R.id.switching_zone, fragmentBeverage).commitAllowingStateLoss();
        }else if (id == R.id.topButtonMeal) {
            transaction.replace(R.id.switching_zone, fragmentMeal).commitAllowingStateLoss();
        }
    }



    private void showCustomVoiceDialog() {
        CustomVoiceRecognitionDialog dialog = new CustomVoiceRecognitionDialog(MenuForUser.this);
        dialog.show();
    }

    private void showCartPopup() {
        ShoppingCartDialog shoppingCartDialog = new ShoppingCartDialog(MenuForUser.this);
        shoppingCartDialog.show();
    }
}
