package com.example.ekavpraxis.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;

import java.util.ArrayList;

public class Forma3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma3);

        // Παραλαβή δεδομένων από Forma2
        String selectedDate  = getIntent().getStringExtra("selectedDate");
        String shift         = getIntent().getStringExtra("shift");
        String medicationWay = getIntent().getStringExtra("medicationWay");
        String medicationStatus = getIntent().getStringExtra("medicationStatus");
        ArrayList<String> selectedTherapies = getIntent().getStringArrayListExtra("selectedTherapies");

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
        ContrastHelper.applyCurrentContrast(this);

        EditText etPatientInitials  = findViewById(R.id.etPatientInitials);
        EditText etPossibleCondition = findViewById(R.id.etPossibleCondition);
        EditText etHospital          = findViewById(R.id.etHospital);

        findViewById(R.id.btnSaveCase).setOnClickListener(v ->
            Toast.makeText(this, "Περιστατικό Αποθηκεύτηκε", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            String patientInitials   = etPatientInitials.getText().toString().trim();
            String possibleCondition = etPossibleCondition.getText().toString().trim();
            String hospital          = etHospital.getText().toString().trim();

            Intent intent = new Intent(Forma3.this, Forma4.class);
            intent.putExtra("selectedDate",      selectedDate);
            intent.putExtra("shift",             shift);
            intent.putExtra("medicationWay",     medicationWay);
            intent.putExtra("medicationStatus",  medicationStatus);
            intent.putStringArrayListExtra("selectedTherapies", selectedTherapies);
            intent.putExtra("patientInitials",   patientInitials);
            intent.putExtra("possibleCondition", possibleCondition);
            intent.putExtra("hospital",          hospital);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
