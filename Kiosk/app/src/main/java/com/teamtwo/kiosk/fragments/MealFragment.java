package com.teamtwo.kiosk.fragments;

import android.graphics.Interpolator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.teamtwo.kiosk.MenuForUser;
import com.teamtwo.kiosk.R;
import com.teamtwo.kiosk.SelectedMenuDetailActivity;
import com.teamtwo.kiosk.ShoppingCartDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MealFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("test", "생성됨");
        return inflater.inflate(R.layout.fragment_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            spreadMenu();
        } catch(Exception e) {
            Log.e("error", e.getMessage());
        }
    }


    public void spreadMenu() throws JSONException {
        LinearLayout fragment_meal_zone = getView().findViewById(R.id.fragment_meal_zone);
        fragment_meal_zone.removeAllViews();
        Typeface customFont = ResourcesCompat.getFont(getContext(), R.font.gmarketsanslight);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View scrollMenuLayout = inflater.inflate(R.layout.fragment_frame, null, false);
        fragment_meal_zone.addView(scrollMenuLayout);

        LinearLayout userMenuScrollView = scrollMenuLayout.findViewById(R.id.frame_list);

        ArrayList<JSONObject> menuList = MenuForUser.menyList;
        Log.d("test", String.valueOf(menuList.size()));
        for(int i = 0; i< menuList.size(); i++) {

            if(menuList.get(i).getString("category1").equals("식사")) {
                // add_menu 레이아웃 인플레이션
                View addMenuLayout = inflater.inflate(R.layout.add_menu, null, false);
                View detail_selecting_window_activity = inflater.inflate(R.layout.detail_seleting_window_activity, null, false);
//                View style_top = inflater.inflate(R.layout.style_top,null,false);
//
//                Button topButtonSnack = style_top.findViewById(R.id.topButtonSnack);
//                Button topButtonLiquor = style_top.findViewById(R.id.topButtonLiquor);
//                Button topButtonBeverage = style_top.findViewById(R.id.topButtonBeverage);
//                Button topButtonMeal = style_top.findViewById(R.id.topButtonMeal);
//
//                topButtonSnack.setTextColor(getResources().getColor(R.color.white));
//                topButtonLiquor.setTextColor(getResources().getColor(R.color.white));
//                topButtonBeverage.setTextColor(getResources().getColor(R.color.white));
//                topButtonMeal.setTextColor(getResources().getColor(R.color.hot_pink));

                ImageView menuImageView;
                TextView menuNameTextView = addMenuLayout.findViewById(R.id.user_menu_name);
                TextView menuPriceTextView = addMenuLayout.findViewById(R.id.user_menu_price);
                TextView menuDetailTextView = addMenuLayout.findViewById(R.id.user_menu_detail);
                Button orderingBtn_temp = detail_selecting_window_activity.findViewById(R.id.orderingBtn_temp);

                LinearLayout inner = addMenuLayout.findViewById(R.id.middle_inner);
                inner.setLayoutParams(new LinearLayout.LayoutParams(
                        570,
                        1516
                ));
                inner.setOrientation(LinearLayout.VERTICAL);

                menuImageView = new ImageView(getContext());
                menuImageView.setId(View.generateViewId());
                menuImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                menuImageView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        870
                ));
                Glide.with(this)
                        .load(menuList.get(i).getString("imageURL"))
                        .into(menuImageView);
                Glide.with(this)
                        .load(menuList.get(i).getString("imageURL"))
                        .into(menuImageView);


                inner.addView(menuImageView, 0);


                menuNameTextView.setText(menuList.get(i).getString("name"));
                menuNameTextView.setTextColor(getResources().getColor(R.color.white));
                menuNameTextView.setTextSize(20);
                menuNameTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                menuNameTextView.setIncludeFontPadding(false);

                menuDetailTextView.setText( menuList.get(i).getString("text"));

                menuPriceTextView.setText(menuList.get(i).getString("price"));
                menuPriceTextView.setTextColor(getResources().getColor(R.color.white));
                menuPriceTextView.setTextSize(20);
                menuPriceTextView.setGravity(Gravity.CENTER);
                menuPriceTextView.setIncludeFontPadding(false);

                userMenuScrollView.addView(addMenuLayout);

                menuImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectedMenuDetailActivity selectedMenuDetailActivity = new SelectedMenuDetailActivity(getContext());
                        selectedMenuDetailActivity.show();

                        selectedMenuDetailActivity.setDetailPopupImage(menuImageView);
                        selectedMenuDetailActivity.setDetail_popup_name(menuNameTextView);
                        selectedMenuDetailActivity.setDetail_popup_script(menuDetailTextView);

                        Button addToCart = selectedMenuDetailActivity.findViewById(R.id.orderingBtn_temp);
                        TextView count_num = selectedMenuDetailActivity.findViewById(R.id.count_num);

                        addToCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String selectedMenuName = menuNameTextView.getText().toString();
                                Integer selectedMenuPrice = Integer.parseInt(menuPriceTextView.getText().toString());
                                Integer selectedMenuCnt = Integer.parseInt(count_num.getText().toString());

                                Log.v("name: ",selectedMenuName);
                                Log.v("cnt: ",selectedMenuCnt.toString());
                                boolean addCartStatus = ShoppingCartDialog.addToCart(selectedMenuName, new int[]{selectedMenuPrice, selectedMenuCnt});
                                if (addCartStatus == false) {
                                    int curCnt = ShoppingCartDialog.addInfo.get(selectedMenuName)[1];
                                    String message = "10개 이상 장바구니에 추가할 수 없습니다. (현재 " + curCnt + "개 담겨있습니다.)";
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                selectedMenuDetailActivity.dismiss();
                            }
                        });
                    }
                });
            }

        }
    }
}