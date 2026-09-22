package com.example.ekavpraxis.ui.student;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EpiskopisiProgrammatos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episkopisi_programmatos);

        LinearLayout containerList = findViewById(R.id.containerList);
        HashMap<String, String> selectedDays = (HashMap<String, String>) getIntent().getSerializableExtra("selectedDays");

        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
        ContrastHelper.applyCurrentContrast(this);

        if (selectedDays != null) {
            Map<String, String> sortedDays = new TreeMap<>((d1, d2) -> {
                String[] p1 = d1.split("/");
                String[] p2 = d2.split("/");
                String s1 = p1[2] + p1[1] + p1[0];
                String s2 = p2[2] + p2[1] + p2[0];
                return s1.compareTo(s2);
            });
            sortedDays.putAll(selectedDays);

            for (Map.Entry<String, String> entry : sortedDays.entrySet()) {
                addDayView(containerList, entry.getKey(), entry.getValue());
            }
        }

        findViewById(R.id.btnFinalSubmit).setOnClickListener(v -> {
            Toast.makeText(this, "Το πρόγραμμα υποβλήθηκε οριστικά!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomepageSpoudasti.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.backArrow).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }

    private void addDayView(LinearLayout container, String date, String shift) {
        boolean highContrast = ContrastHelper.isHighContrast(this);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, 15, 0, 15);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvDate = new TextView(this);
        tvDate.setText(date);
        tvDate.setTextColor(highContrast ? Color.YELLOW : Color.WHITE);
        tvDate.setTextSize(18f);
        tvDate.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvShift = new TextView(this);
        tvShift.setText(shift);
        tvShift.setTextColor(highContrast ? Color.YELLOW : Color.parseColor("#F2FF00"));
        tvShift.setTextSize(18f);
        tvShift.setGravity(Gravity.END);
        tvShift.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f));

        row.addView(tvDate);
        row.addView(tvShift);

        View line = new View(this);
        line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        line.setBackgroundColor(Color.parseColor("#4DF2FF00"));

        container.addView(row);
        container.addView(line);
    }
}
