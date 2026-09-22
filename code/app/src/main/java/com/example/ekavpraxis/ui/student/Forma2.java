package com.example.ekavpraxis.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;

import java.util.ArrayList;

public class Forma2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma2);

        // Παραλαβή δεδομένων από Forma1
        String selectedDate = getIntent().getStringExtra("selectedDate");
        String shift        = getIntent().getStringExtra("shift");

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
        ContrastHelper.applyCurrentContrast(this);

        // EditText
        EditText etMedicationWay = findViewById(R.id.etMedicationWay);

        // CheckBoxes
        CheckBox cbAeragogoXeria = findViewById(R.id.cbAeragogoXeria);
        CheckBox cbAeragogoMesa = findViewById(R.id.cbAeragogoMesa);
        CheckBox cbAkinitopoiisi = findViewById(R.id.cbAkinitopoiisi);
        CheckBox cbAnapnoiMihaniki = findViewById(R.id.cbAnapnoiMihaniki);
        CheckBox cbAnapnoiCpap = findViewById(R.id.cbAnapnoiCpap);
        CheckBox cbAnapnoiMaska = findViewById(R.id.cbAnapnoiMaska);
        CheckBox cbAnapnoiAmbu = findViewById(R.id.cbAnapnoiAmbu);
        CheckBox cbKyklophoriaIv = findViewById(R.id.cbKyklophoriaIv);
        CheckBox cbKyklophoriaPeridesi = findViewById(R.id.cbKyklophoriaPeridesi);
        CheckBox cbFarmakaNai = findViewById(R.id.cbFarmakaNai);
        CheckBox cbFarmakaOxi = findViewById(R.id.cbFarmakaOxi);

        findViewById(R.id.btnNext).setOnClickListener(v -> {

            String medicationWay = etMedicationWay.getText().toString().trim();

            ArrayList<String> selectedTherapies = new ArrayList<>();
            if (cbAeragogoXeria.isChecked()) selectedTherapies.add("ΑΕΡΑΓΩΓΟΣ: ΧΕΡΙΑ");
            if (cbAeragogoMesa.isChecked()) selectedTherapies.add("ΑΕΡΑΓΩΓΟΣ: ΜΕΣΑ");
            if (cbAkinitopoiisi.isChecked()) selectedTherapies.add("ΑΚΙΝΗΤΟΠΟΙΗΣΗ");
            if (cbAnapnoiMihaniki.isChecked()) selectedTherapies.add("ΑΝΑΠΝΟΗ: ΜΗΧΑΝΙΚΗ");
            if (cbAnapnoiCpap.isChecked()) selectedTherapies.add("ΑΝΑΠΝΟΗ: CPAP");
            if (cbAnapnoiMaska.isChecked()) selectedTherapies.add("ΑΝΑΠΝΟΗ: ΜΑΣΚΑ");
            if (cbAnapnoiAmbu.isChecked()) selectedTherapies.add("ΑΝΑΠΝΟΗ: AMBU");
            if (cbKyklophoriaIv.isChecked()) selectedTherapies.add("ΚΥΚΛΟΦΟΡΙΑ: I.V.");
            if (cbKyklophoriaPeridesi.isChecked()) selectedTherapies.add("ΚΥΚΛΟΦΟΡΙΑ: ΠΕΡΙΔΕΣΗ");
            
            String medicationStatus = cbFarmakaNai.isChecked() ? "ΝΑΙ" : "ΟΧΙ";

            Intent intent = new Intent(Forma2.this, Forma3.class);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("shift",        shift);
            intent.putExtra("medicationWay", medicationWay);
            intent.putExtra("medicationStatus", medicationStatus);
            intent.putStringArrayListExtra("selectedTherapies", selectedTherapies);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
