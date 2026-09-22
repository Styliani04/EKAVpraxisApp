package com.example.ekavpraxis.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Forma5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma5);

        // Παραλαβή προηγούμενων δεδομένων
        String selectedDate      = getIntent().getStringExtra("selectedDate");
        String shift             = getIntent().getStringExtra("shift");
        String medicationWay     = getIntent().getStringExtra("medicationWay");
        String medicationStatus  = getIntent().getStringExtra("medicationStatus");
        ArrayList<String> selectedTherapies = getIntent().getStringArrayListExtra("selectedTherapies");
        String patientInitials   = getIntent().getStringExtra("patientInitials");
        String possibleCondition = getIntent().getStringExtra("possibleCondition");
        String hospital          = getIntent().getStringExtra("hospital");
        String ambulance         = getIntent().getStringExtra("ambulance");

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
        ContrastHelper.applyCurrentContrast(this);

        // CheckBoxes για ικανότητες
        CheckBox cbProtovouliaPoly = findViewById(R.id.cbProtovouliaPoly);
        CheckBox cbProtovouliaKala = findViewById(R.id.cbProtovouliaKala);
        CheckBox cbProtovouliaMetria = findViewById(R.id.cbProtovouliaMetria);
        
        CheckBox cbSynergasiaPoly = findViewById(R.id.cbSynergasiaPoly);
        CheckBox cbSynergasiaKala = findViewById(R.id.cbSynergasiaKala);
        CheckBox cbSynergasiaMetria = findViewById(R.id.cbSynergasiaMetria);
        
        CheckBox cbPoiotikiPoly = findViewById(R.id.cbPoiotikiPoly);
        CheckBox cbPoiotikiKala = findViewById(R.id.cbPoiotikiKala);
        CheckBox cbPoiotikiMetria = findViewById(R.id.cbPoiotikiMetria);
        
        CheckBox cbPosotikiPoly = findViewById(R.id.cbPosotikiPoly);
        CheckBox cbPosotikiKala = findViewById(R.id.cbPosotikiKala);
        CheckBox cbPosotikiMetria = findViewById(R.id.cbPosotikiMetria);
        
        CheckBox cbEpimeleiaPoly = findViewById(R.id.cbEpimeleiaPoly);
        CheckBox cbEpimeleiaKala = findViewById(R.id.cbEpimeleiaKala);
        CheckBox cbEpimeleiaMetria = findViewById(R.id.cbEpimeleiaMetria);

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            
            // Συλλογή αξιολογήσεων
            HashMap<String, String> skills = new HashMap<>();
            skills.put("Πρωτοβουλία", getSelectedGrade(cbProtovouliaPoly, cbProtovouliaKala, cbProtovouliaMetria));
            skills.put("Συνεργασία", getSelectedGrade(cbSynergasiaPoly, cbSynergasiaKala, cbSynergasiaMetria));
            skills.put("Ποιοτική Απόδοση", getSelectedGrade(cbPoiotikiPoly, cbPoiotikiKala, cbPoiotikiMetria));
            skills.put("Ποσοτική Απόδοση", getSelectedGrade(cbPosotikiPoly, cbPosotikiKala, cbPosotikiMetria));
            skills.put("Επιμέλεια", getSelectedGrade(cbEpimeleiaPoly, cbEpimeleiaKala, cbEpimeleiaMetria));

            Intent intent = new Intent(Forma5.this, Forma6.class);
            intent.putExtra("selectedDate",      selectedDate);
            intent.putExtra("shift",             shift);
            intent.putExtra("medicationWay",     medicationWay);
            intent.putExtra("medicationStatus",  medicationStatus);
            intent.putStringArrayListExtra("selectedTherapies", selectedTherapies);
            intent.putExtra("patientInitials",   patientInitials);
            intent.putExtra("possibleCondition", possibleCondition);
            intent.putExtra("hospital",          hospital);
            intent.putExtra("ambulance",         ambulance);
            intent.putExtra("skills",            skills); // Pass HashMap
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }

    private String getSelectedGrade(CheckBox poly, CheckBox kala, CheckBox metria) {
        if (poly.isChecked()) return "ΠΟΛΥ ΚΑΛΑ";
        if (kala.isChecked()) return "ΚΑΛΑ";
        if (metria.isChecked()) return "ΜΕΤΡΙΑ";
        return "ΔΕΝ ΟΡΙΣΤΗΚΕ";
    }
}
