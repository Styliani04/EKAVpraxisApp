package com.example.ekavpraxis;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.ui.ContrastHelper;

public class AnartisiAsthenoforon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anartisi_asthenoforon);

        ImageView backArrow = findViewById(R.id.backArrow);
        Button btnSelectFile = findViewById(R.id.btnSelectFile);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);

        backArrow.setOnClickListener(v -> finish());
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));

        btnSelectFile.setOnClickListener(v -> {
            // Εδώ θα έμπαινε ο κώδικας για το άνοιγμα του File Picker
            // και την ανάγνωση του Excel αρχείου
            Toast.makeText(this, "Αναζήτηση αρχείου Excel...", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
