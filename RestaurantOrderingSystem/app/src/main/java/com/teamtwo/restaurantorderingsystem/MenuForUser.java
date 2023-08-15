package com.teamtwo.restaurantorderingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.bumptech.glide.Glide;
import com.teamtwo.restaurantorderingsystem.fragments.BeverageFragment;
import com.teamtwo.restaurantorderingsystem.fragments.LiquorFragment;
import com.teamtwo.restaurantorderingsystem.fragments.MealFragment;
import com.teamtwo.restaurantorderingsystem.fragments.SnackFragment;


import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MenuForUser extends AppCompatActivity {
    private ImageButton voiceImageButton;
    private Button voice_recognition_btn;
    private Button topButtonMeal;
    private Button topButtonSnack;
    private Button topButtonLiquor;
    private Button topButtonBeverage;
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
        voice_recognition_btn = findViewById(R.id.voice_recognition_btn);

        //음성 인식 gif
        voiceImageButton = findViewById(R.id.voiceIcon);

        Glide.with(this).load(R.drawable.white_voice).into(voiceImageButton);

        //Fragment 영역
        fragmentManager = getSupportFragmentManager();

        fragmentBeverage = new BeverageFragment();
        fragmentLiquor = new LiquorFragment();
        fragmentSnack = new SnackFragment();
        fragmentMeal = new MealFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.switching_zone, fragmentSnack).commitAllowingStateLoss();

        voiceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomVoiceDialog();
            }
        });

        voice_recognition_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomVoiceDialog();
            }
        });

//        topButtonMeal = (Button)findViewById(R.id.topButtonMeal);
//        topButtonSnack = (Button)findViewById(R.id.topButtonSnack);
//        topButtonLiquor = (Button)findViewById(R.id.topButtonLiquor);
//        topButtonBeverage = (Button)findViewById(R.id.topButtonBeverage);
//
//        topButtonMeal.setOnClickListener(topButtonsListener);
//        topButtonSnack.setOnClickListener(topButtonsListener);
//        topButtonLiquor.setOnClickListener(topButtonsListener);
//        topButtonBeverage.setOnClickListener(topButtonsListener);
//        topButtonMeal.performClick();  //첫번째 버튼을 눌린 효과를 줌
    }

//    View.OnClickListener topButtonsListener  = new View.OnClickListener(){
//        @Override
//        public void onClick(View view) {
//            if (view.getId() == R.id.topButtonMeal){
//                topButtonMeal.setSelected(true);
//                topButtonSnack.setSelected(false);
//                topButtonLiquor.setSelected(false);
//                topButtonBeverage.setSelected(false);
//            }
//            else if(view.getId() == R.id.topButtonSnack){
//                topButtonMeal.setSelected(false);
//                topButtonSnack.setSelected(true);
//                topButtonLiquor.setSelected(false);
//                topButtonBeverage.setSelected(false);
//            }
//            else if(view.getId() == R.id.topButtonLiquor){
//                topButtonMeal.setSelected(false);
//                topButtonSnack.setSelected(false);
//                topButtonLiquor.setSelected(true);
//                topButtonBeverage.setSelected(false);
//            }
//            else if(view.getId() == R.id.topButtonBeverage){
//                topButtonMeal.setSelected(false);
//                topButtonSnack.setSelected(false);
//                topButtonLiquor.setSelected(false);
//                topButtonBeverage.setSelected(true);
//            }
//        }
//    };




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
}