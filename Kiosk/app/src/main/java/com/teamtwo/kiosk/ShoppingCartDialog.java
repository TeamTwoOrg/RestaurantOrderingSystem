package com.teamtwo.kiosk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ShoppingCartDialog extends Dialog {

    public static HashMap<String, int[]> addInfo = new HashMap<>();
    private Context context;

    public ShoppingCartDialog(Context context){
        super(context);
        this.context = context;
    }

    public static void cartClear() {
        addInfo.clear();
    }

    public static boolean addToCart(String name, int[] info) {
        if (addInfo.containsKey(name)) {
            if (addInfo.get(name)[1] + info[1] > 9) {
                return false;
            }
            addInfo.put(name, new int[]{info[0], addInfo.get(name)[1] + info[1]});
        } else {
            addInfo.put(name, info);
        }

        if (addInfo.get(name)[1] == 0) {
            addInfo.remove(name);
        }

        return true;

//        Log.v("map", "---------");
//        for(String key : addInfo.keySet()) {
//            Log.v("map", key+": "+ addInfo.get(key));
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart);

        ImageButton escDialogButton = findViewById(R.id.escDialogButton); // 추가된 부분
        Button paymentButton = findViewById(R.id.paymentButton);

        escDialogButton.setOnClickListener(new View.OnClickListener() { // 추가된 부분
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView totalPriceTextView = findViewById(R.id.totalPrice);
                int totalPrice = Integer.parseInt(totalPriceTextView.getText().toString());

                if (ShoppingCartDialog.addInfo.isEmpty()) {
                    String message = "장바구니가 비어있습니다.";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    return;
                }

                dismiss();

                // 결제 해야함
                PayActivity payActivity = new PayActivity("주문", String.valueOf(totalPrice));

                Intent intent = new Intent(context, payActivity.getClass());
                context.startActivity(intent);
            }
        });

        addShoppingCartItems();

    }

    public void addShoppingCartItems() {
        // 'shoppingcart_linear' ID를 가진 LinearLayout를 찾습니다.
        LinearLayout shoppingCartLinear = findViewById(R.id.shoppingcart_linear);
        shoppingCartLinear.removeAllViews();

        TextView totalPriceTextView = findViewById(R.id.totalPrice);
        int totalPrice = 0;

        // LayoutInflater를 사용하여 shoppingcart_item.xml을 Java 객체로 변환한다.
        LayoutInflater inflater = LayoutInflater.from(context);

        // 원하는 횟수만큼 shoppingcart_item 레이아웃을 LinearLayout에 추가합니다.
        // 이 예에서는 shoppingcart_item 레이아웃이 5번 추가됩니다.
        for (String menuName : addInfo.keySet()) {
            // shoppingcart_item.xml을 View 객체로 인플레이트(inflate) 합니다.
            View shoppingCartItem = inflater.inflate(R.layout.shoppingcart_item, shoppingCartLinear, false);

            // 새로운 레이아웃 파라미터를 생성합니다. 너비는 match_parent, 높이는 157dp로 설정합니다.
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (157 * context.getResources().getDisplayMetrics().density));

            // 마진을 위, 아래, 좌, 우 각각 30dp로 설정합니다. 마지막 아이템만 아래 마진이 0dp로 설정됩니다.
            int margin = (int) (30 * context.getResources().getDisplayMetrics().density);
            layoutParams.setMargins(margin, margin, margin, margin);

            // 인플레이트 된 View에 레이아웃 파라미터를 적용합니다.
            shoppingCartItem.setLayoutParams(layoutParams);
            ((TextView) shoppingCartItem.findViewById(R.id.menu_name)).setText(menuName);
            ((TextView) shoppingCartItem.findViewById(R.id.itemPriceTextView)).setText(String.valueOf(addInfo.get(menuName)));

            TextView count_nums = shoppingCartItem.findViewById(R.id.count_nums);
            int cnt = addInfo.get(menuName)[1];
            count_nums.setText(String.valueOf(cnt));
            TextView itemPriceTextView = shoppingCartItem.findViewById(R.id.itemPriceTextView);
            int price = addInfo.get(menuName)[0];
            itemPriceTextView.setText(String.valueOf(price * cnt));
            totalPrice += (price*cnt);

            ImageButton minusButton = shoppingCartItem.findViewById(R.id.sminusButton);
            ImageButton plusButton = shoppingCartItem.findViewById(R.id.splusButton);
            Button deleteButton = shoppingCartItem.findViewById(R.id.deleteButton);

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curCount = Integer.parseInt(count_nums.getText().toString());
                    int curTotal = Integer.parseInt(totalPriceTextView.getText().toString());
                    if (curCount > 1) {
                        count_nums.setText(String.valueOf(curCount-1));
                        itemPriceTextView.setText(String.valueOf(price*(curCount-1)));
                        totalPriceTextView.setText(String.valueOf(curTotal - price));
                        addToCart(menuName, new int[]{price, -1});
                    }
                }
            });

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curCount = Integer.parseInt(count_nums.getText().toString());
                    int curTotal = Integer.parseInt(totalPriceTextView.getText().toString());
                    if (curCount < 9) {
                        count_nums.setText(String.valueOf(curCount+1));
                        itemPriceTextView.setText(String.valueOf(price*(curCount+1)));
                        totalPriceTextView.setText(String.valueOf(curTotal + price));
                        addToCart(menuName, new int[]{price, 1});
                    }

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() { // 추가된 부분
                @Override
                public void onClick(View view) {
                    addInfo.remove(menuName);
                    addShoppingCartItems();
                }
            });
            // 인플레이트된 레이아웃을 shoppingCartLinear에 추가합니다.
            shoppingCartLinear.addView(shoppingCartItem);
        }

        totalPriceTextView.setText(String.valueOf(totalPrice));
    }

}
