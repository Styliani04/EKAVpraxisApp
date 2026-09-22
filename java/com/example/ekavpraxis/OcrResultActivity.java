package com.example.ekavpraxis;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.ui.ContrastHelper;

public class OcrResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_result);

        ImageView ivPreview = findViewById(R.id.ivCapturedPreview);
        TextView tvResult = findViewById(R.id.tvOcrDetailedResult);
        Button btnRescan = findViewById(R.id.btnRescan);
        Button btnSubmit = findViewById(R.id.btnSubmitOcr);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);

        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));

        String ocrText = getIntent().getStringExtra("ocrText");
        String imageUriString = getIntent().getStringExtra("imageUri");

        if (ocrText != null) {
            tvResult.setText(ocrText);
        }

        if (imageUriString != null) {
            ivPreview.setImageURI(Uri.parse(imageUriString));
        }

        btnRescan.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            Toast.makeText(this, "Το φύλλο υποβλήθηκε επιτυχώς!", Toast.LENGTH_LONG).show();
            // Εδώ θα μπορούσαμε να προσθέσουμε την αποθήκευση των δεδομένων
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
