package com.example.ekavpraxis.ui.student;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;

public class ProvoliStoicheion extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provoli_stoicheion);

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));

        ContrastHelper.applyCurrentContrast(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
