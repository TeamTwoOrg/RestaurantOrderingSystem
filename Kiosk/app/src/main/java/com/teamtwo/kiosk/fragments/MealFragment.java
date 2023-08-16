package com.teamtwo.kiosk.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.teamtwo.kiosk.MenuForUser;
import com.teamtwo.kiosk.R;

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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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

        LinearLayout userMenuScrollView = scrollMenuLayout.findViewById(R.id.beverage_list);

        ArrayList<JSONObject> menuList = MenuForUser.menyList;
        Log.d("test", String.valueOf(menuList.size()));
        for(int i = 0; i< menuList.size(); i++) {


            // add_menu 레이아웃 인플레이션
            View addMenuLayout = inflater.inflate(R.layout.add_menu, null, false);

            ImageView menuImageButton = addMenuLayout.findViewById(R.id.menu_img);
            TextView menuNameTextView = addMenuLayout.findViewById(R.id.user_menu_name);
            TextView menuPriceTextView = addMenuLayout.findViewById(R.id.user_menu_price);


            Log.d("test", "1");
            LinearLayout inner = addMenuLayout.findViewById(R.id.middle_inner);
            inner.setLayoutParams(new LinearLayout.LayoutParams(
                   300,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            inner.setOrientation(LinearLayout.VERTICAL);

            menuImageButton = new ImageButton(getContext());
            menuImageButton.setId(View.generateViewId());
            menuImageButton.setLayoutParams(new LinearLayout.LayoutParams(
                    300,
                    300,
                    1
            ));
            Glide.with(this)
                    .load(menuList.get(i).getString("imageURL"))
                    .into(menuImageButton);
            inner.addView(menuImageButton, 0);
            Log.d("test", "2");
            menuNameTextView.setText(menuList.get(i).getString("name"));
            menuNameTextView.setTextColor(getResources().getColor(R.color.white));
            menuNameTextView.setTextSize(20);
            menuNameTextView.setGravity(Gravity.CENTER);
            menuNameTextView.setIncludeFontPadding(false);
            Log.d("test", "3");

            menuPriceTextView.setText(menuList.get(i).getString("price"));
            menuPriceTextView.setTextColor(getResources().getColor(R.color.white));
            menuPriceTextView.setTextSize(20);
            menuPriceTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            menuPriceTextView.setIncludeFontPadding(false);
            menuPriceTextView.setPadding(0,5,0,0);
            Log.d("test", "4");

            userMenuScrollView.addView(addMenuLayout);
            Log.d("test", "5");

        }
    }

}