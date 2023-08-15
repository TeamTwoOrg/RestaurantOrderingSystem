package com.teamtwo.restaurantorderingsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import com.bumptech.glide.Glide;
import com.teamtwo.restaurantorderingsystem.fragments.BeverageFragment;
import com.teamtwo.restaurantorderingsystem.fragments.LiquorFragment;
import com.teamtwo.restaurantorderingsystem.fragments.MealFragment;
import com.teamtwo.restaurantorderingsystem.fragments.SnackFragment;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MenuForUser extends AppCompatActivity {
    private ImageButton voiceImageButton;
    private AppCompatButton cartButton;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private BeverageFragment fragmentBeverage;
    private LiquorFragment fragmentLiquor;
    private SnackFragment fragmentSnack;
    private MealFragment fragmentMeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_for_menu);
        voiceImageButton = findViewById(R.id.voiceIcon);
        cartButton = findViewById(R.id.cartbutton);

        Glide.with(this).load(R.drawable.white_voice).into(voiceImageButton);

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
    }

    public void clickHandler(View view) {
        transaction = fragmentManager.beginTransaction();

        int id = view.getId();
        if (id == R.id.topButtonSnack) {
            transaction.replace(R.id.switching_zone, fragmentSnack).commitAllowingStateLoss();
        } else if (id == R.id.topButtonLiquor) {
            transaction.replace(R.id.switching_zone, fragmentLiquor).commitAllowingStateLoss();
        } else if (id == R.id.topButtonBeverage) {
            transaction.replace(R.id.switching_zone, fragmentBeverage).commitAllowingStateLoss();
        } else if (id == R.id.topButtonMeal) {
            transaction.replace(R.id.switching_zone, fragmentMeal).commitAllowingStateLoss();
        }
    }

    private void showCartPopup() {
        ShoppingCartDialog shoppingCartDialog = new ShoppingCartDialog(MenuForUser.this);
        shoppingCartDialog.show();
    }
}
