package com.teamtwo.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TableNumberActivity extends AppCompatActivity {

    public static String set_table_number_string;
    private TextView table_numbering_textview;
    private ImageButton num_1;
    private ImageButton num_2;
    private ImageButton num_3;
    private ImageButton num_4;
    private ImageButton num_5;
    private ImageButton num_6;
    private ImageButton num_7;
    private ImageButton num_8;
    private ImageButton num_9;
    private ImageButton num_0;
    private ImageButton btn_delete_num_all;
    private ImageButton btn_back_space;

    private Button set_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbering_table);

        set_table_number_string = "";

        num_0 = findViewById(R.id.num_0);
        num_1 = findViewById(R.id.num_1);
        num_2 = findViewById(R.id.num_2);
        num_3 = findViewById(R.id.num_3);
        num_4 = findViewById(R.id.num_4);
        num_5 = findViewById(R.id.num_5);
        num_6 = findViewById(R.id.num_6);
        num_7 = findViewById(R.id.num_7);
        num_8 = findViewById(R.id.num_8);
        num_9 = findViewById(R.id.num_9);

        btn_delete_num_all = findViewById(R.id.btn_delete_num_all);
        btn_back_space = findViewById(R.id.btn_back_space);
        table_numbering_textview = findViewById(R.id.table_numbering_textview);
        set_number = findViewById(R.id.set_number);

        num_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { if(!set_table_number_string.equals("")){ set_table_number_string+=0; table_numbering_textview.setText(set_table_number_string);} }
        });

        num_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { set_table_number_string+=1; table_numbering_textview.setText(set_table_number_string);}
        });

        num_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { set_table_number_string+=2; table_numbering_textview.setText(set_table_number_string);}
        });

        num_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { set_table_number_string+=3; table_numbering_textview.setText(set_table_number_string);}
        });

        num_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { set_table_number_string+=4; table_numbering_textview.setText(set_table_number_string);}
        });

        num_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { set_table_number_string+=5; table_numbering_textview.setText(set_table_number_string);}
        });

        num_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { set_table_number_string+=6; table_numbering_textview.setText(set_table_number_string);}
        });

        num_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { set_table_number_string+=7; table_numbering_textview.setText(set_table_number_string);}
        });

        num_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { set_table_number_string+=8; table_numbering_textview.setText(set_table_number_string);}
        });

        num_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { set_table_number_string+=9; table_numbering_textview.setText(set_table_number_string);}
        });

        btn_back_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { if(set_table_number_string.length()>0){set_table_number_string = set_table_number_string.substring(0,set_table_number_string.length()-1); table_numbering_textview.setText(set_table_number_string);}}
        });

        btn_delete_num_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { set_table_number_string=""; table_numbering_textview.setText(set_table_number_string);}
        });

        set_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!set_table_number_string.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), MenuForUser.class);
                    startActivity(intent);
                }
            }
        });
    }
}
