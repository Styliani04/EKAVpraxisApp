package com.example.ekavpraxis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.ui.ContrastHelper;
import com.example.ekavpraxis.ui.auth.LoginSpoudasti;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        Button btnStudent = findViewById(R.id.btnStudent);
        Button btnAdmin = findViewById(R.id.btnAdmin);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);

        btnStudent.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomePage.this, LoginSpoudasti.class);
            startActivity(intent);
        });

        btnAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomePage.this, LoginGrammateias.class);
            startActivity(intent);
        });

        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Επανεφαρμογή σε περίπτωση που άλλαξε σε άλλη οθόνη
        ContrastHelper.applyCurrentContrast(this);
    }
}
