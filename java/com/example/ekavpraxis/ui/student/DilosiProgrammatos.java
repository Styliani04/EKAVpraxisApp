package com.example.ekavpraxis.ui.student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;

import com.example.ekavpraxis.data.FirestoreRepository;
import com.example.ekavpraxis.data.ScheduleEvent;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DilosiProgrammatos extends AppCompatActivity {

    private HashMap<String, String> selectedDays = new HashMap<>();
    private TextView tvSelectedCount;
    private CalendarView calendarView;
    private final FirestoreRepository repository = new FirestoreRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dilosi_programmatos);

        calendarView = findViewById(R.id.calendarView);
        tvSelectedCount = findViewById(R.id.tvSelectedCount);
        ImageView backArrow = findViewById(R.id.backArrow);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
        ContrastHelper.applyCurrentContrast(this);

        backArrow.setOnClickListener(v -> finish());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeek == Calendar.SUNDAY) {
                Toast.makeText(this, "Δεν επιτρέπεται η εργασία την Κυριακή", Toast.LENGTH_SHORT).show();
                return;
            }

            showShiftDialog(year, month, dayOfMonth);
        });

        findViewById(R.id.btnSubmitSchedule).setOnClickListener(v -> {
            if (selectedDays.isEmpty()) {
                Toast.makeText(this, "Παρακαλώ επιλέξτε τουλάχιστον μία ημέρα", Toast.LENGTH_SHORT).show();
                return;
            }
            
            saveProgram();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }

    private void saveProgram() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        int total = selectedDays.size();
        final int[] saved = {0};

        Toast.makeText(this, "Αποθήκευση προγράμματος στο cloud...", Toast.LENGTH_SHORT).show();

        for (Map.Entry<String, String> entry : selectedDays.entrySet()) {
            ScheduleEvent event = new ScheduleEvent(
                null, 
                entry.getValue(), 
                entry.getKey(),   
                "ΕΚΑΒ",           
                entry.getValue()  
            );

            repository.addScheduleEvent(uid, event, new FirestoreRepository.SimpleCallback() {
                @Override
                public void onSuccess() {
                    saved[0]++;
                    if (saved[0] == total) {
                        Toast.makeText(DilosiProgrammatos.this, "Το πρόγραμμα αποθηκεύτηκε επιτυχώς!", Toast.LENGTH_SHORT).show();
                        
                        // Local backup
                        SharedPreferences prefs = getSharedPreferences("UserProgram", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        JSONObject jsonObject = new JSONObject(selectedDays);
                        editor.putString("programJson", jsonObject.toString());
                        editor.apply();

                        Intent intent = new Intent(DilosiProgrammatos.this, EpiskopisiProgrammatos.class);
                        intent.putExtra("selectedDays", selectedDays);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(DilosiProgrammatos.this, "Σφάλμα αποθήκευσης: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showShiftDialog(int year, int month, int dayOfMonth) {
        String dateKey = String.format("%02d/%02d/%d", dayOfMonth, (month + 1), year);
        String[] shifts = {"Πρωινή Βάρδια", "Απογευματινή Βάρδια", "Αφαίρεση Επιλογής"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Επιλογή Βάρδιας για " + dateKey);
        builder.setItems(shifts, (dialog, which) -> {
            if (which == 2) {
                selectedDays.remove(dateKey);
            } else {
                if (!selectedDays.containsKey(dateKey) && getDaysInWeek(year, month, dayOfMonth) >= 5) {
                    Toast.makeText(this, "Έχετε ήδη δηλώσει 5 ημέρες για αυτή την εβδομάδα", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedDays.put(dateKey, shifts[which]);
            }
            updateStatusText();
        });
        builder.show();
    }

    private int getDaysInWeek(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        
        int count = 0;
        for (String date : selectedDays.keySet()) {
            String[] parts = date.split("/");
            Calendar d = Calendar.getInstance();
            d.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[0]));
            if (d.get(Calendar.WEEK_OF_YEAR) == weekOfYear) {
                count++;
            }
        }
        return count;
    }

    private void updateStatusText() {
        tvSelectedCount.setText("Επιλεγμένες ημέρες: " + selectedDays.size());
    }
}
