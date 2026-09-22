package com.example.ekavpraxis.ui.student;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.ekavpraxis.R;
import com.example.ekavpraxis.CloudVisionOcrHelper;
import com.example.ekavpraxis.OcrResultActivity;
import com.example.ekavpraxis.ui.ContrastHelper;

public class AnartisiFyllou extends AppCompatActivity {

    private Uri imageUri;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anartisi_fyllou);

        ImageView backArrow = findViewById(R.id.backArrow);
        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);

        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));

        // Launcher για την κάμερα
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        performOcrAndNavigate(imageUri);
                    } else {
                        Toast.makeText(this, "Η λήψη ακυρώθηκε", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Launcher για την άδεια κάμερας
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        dispatchTakePictureIntent();
                    } else {
                        Toast.makeText(this, "Απαιτείται άδεια κάμερας για το σκανάρισμα", Toast.LENGTH_LONG).show();
                    }
                }
        );

        findViewById(R.id.btnFillForm).setOnClickListener(v -> {
            Intent intent = new Intent(AnartisiFyllou.this, Forma1.class);
            startActivity(intent);
        });
            
        findViewById(R.id.btnScanDoc).setOnClickListener(v -> {
            checkCameraPermissionAndLaunch();
        });

        backArrow.setOnClickListener(v -> finish());

    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }

    private void checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            File photoFile = createImageFile();
            imageUri = FileProvider.getUriForFile(
                    this,
                    "com.example.ekavpraxis.fileprovider",
                    photoFile
            );
            takePictureLauncher.launch(imageUri);
        } catch (IOException ex) {
            Toast.makeText(this, "Σφάλμα δημιουργίας αρχείου εικόνας", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("SCAN_" + timeStamp, ".jpg", storageDir);
    }

    private void performOcrAndNavigate(Uri uri) {
        Toast.makeText(this, "Αναγνώριση κειμένου σε εξέλιξη...", Toast.LENGTH_SHORT).show();

        // Απενεργοποίηση κουμπιών ώστε να μην πατηθούν ξανά
        findViewById(R.id.btnScanDoc).setEnabled(false);

        CloudVisionOcrHelper.recognizeText(this, uri, new CloudVisionOcrHelper.OcrCallback() {
            @Override
            public void onSuccess(String extractedText) {
                runOnUiThread(() -> {
                    findViewById(R.id.btnScanDoc).setEnabled(true);
                    Intent intent = new Intent(AnartisiFyllou.this, OcrResultActivity.class);
                    intent.putExtra("ocrText", extractedText);
                    intent.putExtra("imageUri", uri.toString());
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    findViewById(R.id.btnScanDoc).setEnabled(true);
                    Toast.makeText(AnartisiFyllou.this,
                            "Σφάλμα: " + errorMessage,
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
