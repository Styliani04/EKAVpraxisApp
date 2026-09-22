package com.example.ekavpraxis.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;

import java.util.ArrayList;

public class VirtualSignatureSp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_signature_sp);

        // Παραλαβή όλων των δεδομένων από Forma4
        String selectedDate      = getIntent().getStringExtra("selectedDate");
        String shift             = getIntent().getStringExtra("shift");
        String medicationWay     = getIntent().getStringExtra("medicationWay");
        String medicationStatus  = getIntent().getStringExtra("medicationStatus");
        ArrayList<String> selectedTherapies = getIntent().getStringArrayListExtra("selectedTherapies");
        String patientInitials   = getIntent().getStringExtra("patientInitials");
        String possibleCondition = getIntent().getStringExtra("possibleCondition");
        String hospital          = getIntent().getStringExtra("hospital");
        String ambulance         = getIntent().getStringExtra("ambulance");

        ImageButton btnConfirm = findViewById(R.id.btnConfirm);
        ImageButton btnCancel  = findViewById(R.id.btnCancel);
        ImageView backArrow    = findViewById(R.id.backArrow);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);

        backArrow.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));

        btnConfirm.setOnClickListener(v -> {
            // Υπογραφή σπουδαστή επιβεβαιώθηκε → συνέχεια στη Forma5
            Intent intent = new Intent(VirtualSignatureSp.this, Forma5.class);
            intent.putExtra("selectedDate",      selectedDate);
            intent.putExtra("shift",             shift);
            intent.putExtra("medicationWay",     medicationWay);
            intent.putExtra("medicationStatus",  medicationStatus);
            intent.putStringArrayListExtra("selectedTherapies", selectedTherapies);
            intent.putExtra("patientInitials",   patientInitials);
            intent.putExtra("possibleCondition", possibleCondition);
            intent.putExtra("hospital",          hospital);
            intent.putExtra("ambulance",         ambulance);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
