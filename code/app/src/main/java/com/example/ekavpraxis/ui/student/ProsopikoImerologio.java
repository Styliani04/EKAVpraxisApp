package com.example.ekavpraxis.ui.student;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;

public class ProsopikoImerologio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prosopiko_imerologio);

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));

        ContrastHelper.applyCurrentContrast(this);

        // Logic for hours calculation
        int workedHours = 120; 
        int totalHours = 960;
        int remainingHours = totalHours - workedHours;

        TextView tvWorkedHours = findViewById(R.id.tvWorkedHours);
        TextView tvRemainingHours = findViewById(R.id.tvRemainingHours);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        tvWorkedHours.setText("Ώρες που πραγματοποιήθηκαν: " + workedHours + " / " + totalHours);
        tvRemainingHours.setText("Απομένουν: " + remainingHours + " ώρες");
        
        int progress = (int) (((float) workedHours / totalHours) * 100);
        progressBar.setProgress(progress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
