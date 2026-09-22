package com.example.ekavpraxis.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.data.FirestoreRepository;
import com.example.ekavpraxis.data.ScheduleEvent;
import com.example.ekavpraxis.ui.ContrastHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Forma1 extends AppCompatActivity {

    private String selectedDate = "";
    private CalendarView calendarView;
    private RadioGroup rgShift;
    private View btnNext;
    private final FirestoreRepository repository = new FirestoreRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma1);

        calendarView = findViewById(R.id.calendarView);
        rgShift = findViewById(R.id.rgShift);
        btnNext = findViewById(R.id.btnNext);
        ImageView backArrow = findViewById(R.id.backArrow);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        ContrastHelper.applyCurrentContrast(this);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
        backArrow.setOnClickListener(v -> finish());

        // Αρχικοποίηση ημερομηνίας
        Calendar c = Calendar.getInstance();
        updateSelectedDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            updateSelectedDate(year, month, dayOfMonth);
        });

        btnNext.setOnClickListener(v -> {
            int selectedId = rgShift.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Παρακαλώ επιλέξτε βάρδια", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton rb = findViewById(selectedId);
            String selectedShift = rb.getText().toString(); // "Πρωινή" ή "Απογευματινή"

            verifyScheduleAndProceed(selectedDate, selectedShift);
        });
    }

    private void verifyScheduleAndProceed(String date, String shift) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnNext.setEnabled(false);
        Toast.makeText(this, "Έλεγχος προγράμματος...", Toast.LENGTH_SHORT).show();

        repository.getSchedule(uid, new FirestoreRepository.ScheduleCallback() {
            @Override
            public void onSuccess(List<ScheduleEvent> list) {
                boolean found = false;
                for (ScheduleEvent event : list) {
                    // Normalize comparison: remove " Βάρδια" if present and compare
                    String eventShift = event.getShift().replace(" Βάρδια", "").trim();
                    String currentShift = shift.replace(" Βάρδια", "").trim();
                    
                    if (event.getDate().equals(date) && eventShift.equalsIgnoreCase(currentShift)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    Intent intent = new Intent(Forma1.this, Forma2.class);
                    intent.putExtra("selectedDate", date);
                    intent.putExtra("shift", shift);
                    startActivity(intent);
                } else {
                    Toast.makeText(Forma1.this, "Σφάλμα: Δεν έχετε δηλώσει βάρδια " + shift + " για την ημερομηνία " + date, Toast.LENGTH_LONG).show();
                    btnNext.setEnabled(true);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(Forma1.this, "Σφάλμα βάσης: " + message, Toast.LENGTH_SHORT).show();
                btnNext.setEnabled(true);
            }
        });
    }

    private void updateSelectedDate(int year, int month, int dayOfMonth) {
        selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
