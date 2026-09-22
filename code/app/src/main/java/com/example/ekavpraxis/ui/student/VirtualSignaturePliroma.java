package com.example.ekavpraxis.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.data.StudentForm;
import com.example.ekavpraxis.ui.ContrastHelper;
import com.example.ekavpraxis.ui.student.StudentViewModel; // Προσθήκη αυτής της γραμμής
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VirtualSignaturePliroma extends AppCompatActivity {

    private StudentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_signature_pliroma);

        // Αρχικοποίηση ViewModel για σύνδεση με το Firestore
        viewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        // Παραλαβή ΟΛΩΝ των δεδομένων που συγκεντρώθηκαν από όλες τις φόρμες
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
        String observations      = getIntent().getStringExtra("observations");
        String crewName          = getIntent().getStringExtra("crewName");

        ImageButton btnConfirm = findViewById(R.id.btnConfirm);
        ImageButton btnCancel  = findViewById(R.id.btnCancel);
        ImageView backArrow    = findViewById(R.id.backArrow);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);

        backArrow.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));

        // Παρακολούθηση (Observation) του αποτελέσματος της αποθήκευσης
        viewModel.isFormSaved().observe(this, saved -> {
            if (saved) {
                Toast.makeText(this, "Η φόρμα αποθηκεύτηκε επιτυχώς στο Cloud!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VirtualSignaturePliroma.this, HomepageSpoudasti.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null)
                Toast.makeText(this, "Σφάλμα κατά την αποθήκευση: " + error, Toast.LENGTH_LONG).show();
        });

        btnConfirm.setOnClickListener(v -> {
            // Δημιουργία του αντικειμένου περιεχομένου (Backend Payload)
            Map<String, Object> content = new HashMap<>();
            content.put("selectedDate",      selectedDate);
            content.put("shift",             shift);
            content.put("medicationWay",     medicationWay);
            content.put("medicationStatus",  medicationStatus);
            content.put("selectedTherapies", selectedTherapies);
            content.put("patientInitials",   patientInitials);
            content.put("possibleCondition", possibleCondition);
            content.put("hospital",          hospital);
            content.put("ambulance",         ambulance);
            content.put("skills",            skills);
            content.put("observations",      observations);
            content.put("crewName",          crewName);

            // Λήψη στοιχείων χρήστη από Firebase Auth
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                String uid   = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                // Δημιουργία της φόρμας
                StudentForm form = new StudentForm(
                    "",           // id (θα δημιουργηθεί από το Firestore)
                    uid,          // studentUid
                    email,        // studentEmail
                    "DAILY_PRACTICE", // type
                    content,      // Τα δεδομένα που μαζέψαμε
                    false,        // scanned
                    System.currentTimeMillis() // timestamp
                );

                // Κλήση του ViewModel για αποθήκευση (Front-to-Back call)
                viewModel.saveForm(form);
            } else {
                Toast.makeText(this, "Σφάλμα: Δεν βρέθηκε συνδεδεμένος χρήστης", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
