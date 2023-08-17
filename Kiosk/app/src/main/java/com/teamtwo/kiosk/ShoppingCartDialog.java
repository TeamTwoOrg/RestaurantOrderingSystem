package com.teamtwo.kiosk;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

public class ShoppingCartDialog extends Dialog {

    public static HashMap<String, Integer> addInfo = new HashMap<>();
    private TextView count_nums;
    private int price;
    private TextView itemPriceTextView;
    private int currentValue = 1;
    private Context context;

    public ShoppingCartDialog(Context context){
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart);

        count_nums = findViewById(R.id.count_nums);
        itemPriceTextView = findViewById(R.id.itemPriceTextView);
        price = Integer.parseInt(itemPriceTextView.getText().toString());

        ImageButton minusButton = findViewById(R.id.sminusButton);
        ImageButton plusButton = findViewById(R.id.splusButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        ImageButton escDialogButton = findViewById(R.id.escDialogButton); // 추가된 부분

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentValue > 1) {
                    currentValue--;
                    count_nums.setText(String.valueOf(currentValue));
                    itemPriceTextView.setText(String.valueOf(price*currentValue));
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentValue++;
                count_nums.setText(String.valueOf(currentValue));
                itemPriceTextView.setText(String.valueOf(price*currentValue));
            }
        });

        escDialogButton.setOnClickListener(new View.OnClickListener() { // 추가된 부분
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() { // 추가된 부분
            @Override
            public void onClick(View view) {
                 //삭제 버튼을 누르면 항목 삭제
            }
        });
    }

}
