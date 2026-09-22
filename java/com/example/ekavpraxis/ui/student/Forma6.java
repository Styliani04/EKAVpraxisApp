package com.example.ekavpraxis.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;
import com.example.ekavpraxis.ui.student.VirtualSignaturePliroma; // Προσθήκη αυτής της γραμμής

import java.util.ArrayList;
import java.util.HashMap;

public class Forma6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma6);

        // Παραλαβή όλων των δεδομένων
        String selectedDate      = getIntent().getStringExtra("selectedDate");
        String shift             = getIntent().getStringExtra("shift");
        String medicationWay     = getIntent().getStringExtra("medicationWay");
        String medicationStatus  = getIntent().getStringExtra("medicationStatus");
        ArrayList<String> selectedTherapies = getIntent().getStringArrayListExtra("selectedTherapies");
        String patientInitials   = getIntent().getStringExtra("patientInitials");
        String possibleCondition = getIntent().getStringExtra("possibleCondition");
        String hospital          = getIntent().getStringExtra("hospital");
        String ambulance         = getIntent().getStringExtra("ambulance");
        HashMap<String, String> skills = (HashMap<String, String>) getIntent().getSerializableExtra("skills");

        ImageView backArrow      = findViewById(R.id.backArrow);
        EditText etObservations  = findViewById(R.id.etObservations);
        EditText etCrewName      = findViewById(R.id.etCrewName);

        backArrow.setOnClickListener(v -> finish());

        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
        ContrastHelper.applyCurrentContrast(this);

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            String observations = etObservations.getText().toString().trim();
            String crewName     = etCrewName.getText().toString().trim();

            Intent intent = new Intent(Forma6.this, VirtualSignaturePliroma.class);
            intent.putExtra("selectedDate",      selectedDate);
            intent.putExtra("shift",             shift);
            intent.putExtra("medicationWay",     medicationWay);
            intent.putExtra("medicationStatus",  medicationStatus);
            intent.putStringArrayListExtra("selectedTherapies", selectedTherapies);
            intent.putExtra("patientInitials",   patientInitials);
            intent.putExtra("possibleCondition", possibleCondition);
            intent.putExtra("hospital",          hospital);
            intent.putExtra("ambulance",         ambulance);
            intent.putExtra("skills",            skills);
            intent.putExtra("observations",      observations);
            intent.putExtra("crewName",          crewName);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
