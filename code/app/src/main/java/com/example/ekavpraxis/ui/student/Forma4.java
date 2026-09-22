package com.example.ekavpraxis.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;

import java.util.ArrayList;

public class Forma4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma4);

        // Παραλαβή δεδομένων από Forma3
        String selectedDate      = getIntent().getStringExtra("selectedDate");
        String shift             = getIntent().getStringExtra("shift");
        String medicationWay     = getIntent().getStringExtra("medicationWay");
        String medicationStatus  = getIntent().getStringExtra("medicationStatus");
        ArrayList<String> selectedTherapies = getIntent().getStringArrayListExtra("selectedTherapies");
        String patientInitials   = getIntent().getStringExtra("patientInitials");
        String possibleCondition = getIntent().getStringExtra("possibleCondition");
        String hospital          = getIntent().getStringExtra("hospital");

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
        ContrastHelper.applyCurrentContrast(this);

        EditText etAmbulance = findViewById(R.id.etAmbulance);
        EditText etShift     = findViewById(R.id.etShift);

        // Η βάρδια είναι προ-συμπληρωμένη και κλειδωμένη
        if (shift != null) {
            etShift.setText(shift);
            etShift.setFocusable(false);
            etShift.setEnabled(false);
        }

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            String ambulance = etAmbulance.getText().toString().trim();

            Intent intent = new Intent(Forma4.this, VirtualSignatureSp.class);
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
