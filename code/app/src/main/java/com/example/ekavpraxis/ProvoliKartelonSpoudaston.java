package com.example.ekavpraxis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.ui.ContrastHelper;

public class ProvoliKartelonSpoudaston extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provoli_kartelon_spoudaston);

        ImageView backArrow = findViewById(R.id.backArrow);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);

        backArrow.setOnClickListener(v -> finish());
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));

        // Πλοήγηση στις λεπτομέρειες των σπουδαστών (Πίνακας Φύλλων Πρακτικής)
        findViewById(R.id.btnDetails1).setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentDetailsTable.class);
            intent.putExtra("studentName", "Ιωάννης Παπαδόπουλος");
            startActivity(intent);
        });

        findViewById(R.id.btnDetails2).setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentDetailsTable.class);
            intent.putExtra("studentName", "Μαρία Κωνσταντίνου");
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
