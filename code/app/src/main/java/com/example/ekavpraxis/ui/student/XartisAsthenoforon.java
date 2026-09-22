package com.example.ekavpraxis.ui.student;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ekavpraxis.data.FirestoreRepository;
import com.example.ekavpraxis.data.ScheduleEvent;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class XartisAsthenoforon extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CardView ambulanceCard;
    private TextView tvAmbulanceCode, tvAmbulanceArea, tvAmbulanceShift;
    private Marker selectedMarker;
    private final FirestoreRepository repository = new FirestoreRepository();

    private static class Ambulance {
        String code;
        String area;
        String shiftType;
        LatLng position;

        Ambulance(String code, String area, String shiftType, LatLng position) {
            this.code = code;
            this.area = area;
            this.shiftType = shiftType;
            this.position = position;
        }
    }

    private List<Ambulance> ambulanceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xartis_asthenoforon);

        ambulanceCard = findViewById(R.id.ambulanceCard);
        tvAmbulanceCode = findViewById(R.id.tvAmbulanceCode);
        tvAmbulanceArea = findViewById(R.id.tvAmbulanceArea);
        tvAmbulanceShift = findViewById(R.id.tvAmbulanceShift);
        ImageView btnCloseCard = findViewById(R.id.btnCloseCard);
        Button btnCommitAmbulance = findViewById(R.id.btnCommitAmbulance);
        ImageView backArrow = findViewById(R.id.backArrow);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));
        ContrastHelper.applyCurrentContrast(this);

        ambulanceList.add(new Ambulance("ΑΣΘ-102", "Σύνταγμα", "Γρήγορη", new LatLng(37.9756, 23.7348)));
        ambulanceList.add(new Ambulance("ΑΣΘ-205", "Ομόνοια", "Αργή", new LatLng(37.9838, 23.7275)));
        ambulanceList.add(new Ambulance("ΑΣΘ-310", "Καλλιθέα", "Γρήγορη", new LatLng(37.9548, 23.6967)));
        ambulanceList.add(new Ambulance("ΑΣΘ-412", "Χαλάνδρι", "Αργή", new LatLng(38.0214, 23.7992)));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnCloseCard.setOnClickListener(v -> ambulanceCard.setVisibility(View.GONE));
        backArrow.setOnClickListener(v -> finish());

        btnCommitAmbulance.setOnClickListener(v -> checkAndCommit());
    }

    private void checkAndCommit() {
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Toast.makeText(this, "Έλεγχος βάρδιας στο cloud...", Toast.LENGTH_SHORT).show();

        repository.getSchedule(uid, new FirestoreRepository.ScheduleCallback() {
            @Override
            public void onSuccess(List<ScheduleEvent> list) {
                String foundShift = null;
                for (ScheduleEvent event : list) {
                    if (event.getDate().equals(today)) {
                        foundShift = event.getShift();
                        break;
                    }
                }

                if (foundShift != null) {
                    Toast.makeText(XartisAsthenoforon.this, "Επιτυχής δέσμευση! Βάρδια: " + foundShift, Toast.LENGTH_LONG).show();
                    ambulanceCard.setVisibility(View.GONE);
                    if (selectedMarker != null) selectedMarker.remove();
                } else {
                    Toast.makeText(XartisAsthenoforon.this, "Σφάλμα: Δεν βρέθηκε καταχωρημένη βάρδια για σήμερα (" + today + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(XartisAsthenoforon.this, "Σφάλμα βάσης: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng athens = new LatLng(37.9838, 23.7275);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(athens, 12f));

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ambulance);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

        for (Ambulance amb : ambulanceList) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(amb.position)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
            if (marker != null) marker.setTag(amb);
        }

        mMap.setOnMarkerClickListener(marker -> {
            selectedMarker = marker;
            Ambulance amb = (Ambulance) marker.getTag();
            if (amb != null) {
                tvAmbulanceCode.setText("Κωδικός: " + amb.code);
                tvAmbulanceArea.setText("Περιοχή: " + amb.area);
                tvAmbulanceShift.setText("Βάρδια: " + amb.shiftType);
                ambulanceCard.setVisibility(View.VISIBLE);
            }
            return false;
        });

        mMap.setOnMapClickListener(latLng -> ambulanceCard.setVisibility(View.GONE));
    }
}
