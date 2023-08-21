package com.teamtwo.kiosk;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class SelectedMenuDetailActivity extends Dialog {
    private ImageView detail_popup_image;
    private TextView detail_popup_name;
    private TextView detail_popup_script;
    private TextView count_num;//카운트
    private int currentValue = 1; //기본
    private Context context;

    public SelectedMenuDetailActivity(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_seleting_window_activity);

        detail_popup_image = findViewById(R.id.detail_popup_image);
        detail_popup_name = findViewById(R.id.detail_popup_name);
        detail_popup_script = findViewById(R.id.detail_popup_script);

        // 카운트 숫자
        count_num = findViewById(R.id.count_num);

        ImageButton minusButton = findViewById(R.id.minus);
        ImageButton plusButton = findViewById(R.id.plus);

        ImageButton closeButton = findViewById(R.id.escButton); //닫기 버튼

        //빼기
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentValue > 1) {
                    currentValue--;
                    count_num.setText(String.valueOf(currentValue));
                }
            }
        });

        //더하기
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentValue < 9) {
                    currentValue++;
                    count_num.setText(String.valueOf(currentValue));
                }
            }
        });

        //닫힘
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
    public void setDetailPopupImage(ImageView menuImageVIew) {
        detail_popup_image.setImageDrawable(menuImageVIew.getDrawable());
    }

    public void setDetail_popup_name(TextView detail_popup_name){
        this.detail_popup_name.setText(detail_popup_name.getText().toString());
    }

    public void setDetail_popup_name(String detail_popup_name){
        this.detail_popup_name.setText(detail_popup_name);
    }

    public void setDetail_popup_script(TextView detail_popup_script){
        this.detail_popup_script.setText(detail_popup_script.getText().toString());
    }
    public void setDetail_popup_script(String detail_popup_script){
        this.detail_popup_script.setText(detail_popup_script);
    }

}