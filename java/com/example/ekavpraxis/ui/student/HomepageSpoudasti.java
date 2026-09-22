package com.example.ekavpraxis.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;
import com.example.ekavpraxis.ui.auth.LoginSpoudasti;

public class HomepageSpoudasti extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_spoudasti);

        ImageView backArrow = findViewById(R.id.backArrow);
        ImageView profilIcon = findViewById(R.id.profilIcon);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);
        
        findViewById(R.id.btnMap).setOnClickListener(v -> {
            Intent intent = new Intent(HomepageSpoudasti.this, XartisAsthenoforon.class);
            startActivity(intent);
        });
            
        findViewById(R.id.btnUpload).setOnClickListener(v -> {
            Intent intent = new Intent(HomepageSpoudasti.this, AnartisiFyllou.class);
            startActivity(intent);
        });
            
        findViewById(R.id.btnSchedule).setOnClickListener(v -> {
            Intent intent = new Intent(HomepageSpoudasti.this, DilosiProgrammatos.class);
            startActivity(intent);
        });

        backArrow.setOnClickListener(v -> finish());
        profilIcon.setOnClickListener(this::showProfilePopup);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }

    private void showProfilePopup(View anchor) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_profile_menu, null);
        
        PopupWindow popupWindow = new PopupWindow(popupView, 
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                true);

        popupView.findViewById(R.id.menuProvoliStoicheion).setOnClickListener(v -> {
            popupWindow.dismiss();
            startActivity(new Intent(this, ProvoliStoicheion.class));
        });

        popupView.findViewById(R.id.menuProsopikoImerologio).setOnClickListener(v -> {
            popupWindow.dismiss();
            startActivity(new Intent(this, ProsopikoImerologio.class));
        });

        popupView.findViewById(R.id.menuAposyndesi).setOnClickListener(v -> {
            popupWindow.dismiss();
            Intent intent = new Intent(this, LoginSpoudasti.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        popupWindow.setElevation(10);
        popupWindow.showAsDropDown(anchor, -150, 0);
    }
}
