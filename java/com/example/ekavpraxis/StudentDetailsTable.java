package com.example.ekavpraxis;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.ui.ContrastHelper;

public class StudentDetailsTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details_table);

        ImageView backArrow = findViewById(R.id.backArrow);
        TextView tvStudentName = findViewById(R.id.tvStudentName);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);

        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));

        // Λήψη ονόματος σπουδαστή αν έχει περαστεί μέσω Intent
        String studentName = getIntent().getStringExtra("studentName");
        if (studentName != null) {
            tvStudentName.setText("Φύλλα Πρακτικής: " + studentName);
        }

        backArrow.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
