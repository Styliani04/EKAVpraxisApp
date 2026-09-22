package com.example.ekavpraxis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.ui.ContrastHelper;

public class HomepageGrammateias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_grammateias);

        Button btnViewCards = findViewById(R.id.btnViewCards);
        Button btnPostAmbulances = findViewById(R.id.btnPostAmbulances);
        ImageView backArrow = findViewById(R.id.backArrow);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);

        btnViewCards.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageGrammateias.this, ProvoliKartelonSpoudaston.class);
            startActivity(intent);
        });

        btnPostAmbulances.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageGrammateias.this, AnartisiAsthenoforon.class);
            startActivity(intent);
        });

        backArrow.setOnClickListener(v -> finish());

        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
