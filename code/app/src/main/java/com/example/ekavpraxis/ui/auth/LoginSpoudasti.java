package com.example.ekavpraxis.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ekavpraxis.ui.student.HomepageSpoudasti;
import com.example.ekavpraxis.R;
import com.example.ekavpraxis.ui.ContrastHelper;

public class LoginSpoudasti extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private EditText etEmail, etPassword;
    private FrameLayout btnLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_spoudasti);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        etEmail = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = new ProgressBar(this);

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> finish());

        ImageView brightnessIcon = findViewById(R.id.brightnessIcon);
        brightnessIcon.setOnClickListener(v -> ContrastHelper.toggleContrast(this));

        // Εφαρμογή Contrast
        ContrastHelper.applyCurrentContrast(this);

        // Παρακολούθηση κατάστασης login
        authViewModel.getUiState().observe(this, state -> {
            if (state instanceof AuthViewModel.AuthUiState.Success) {
                Intent intent = new Intent(LoginSpoudasti.this, HomepageSpoudasti.class);
                startActivity(intent);
                finish();
            } else if (state instanceof AuthViewModel.AuthUiState.Error) {
                String message = ((AuthViewModel.AuthUiState.Error) state).message;
                Toast.makeText(this, "Σφάλμα Σύνδεσης: " + message, Toast.LENGTH_LONG).show();
                btnLogin.setEnabled(true);
            } else if (state instanceof AuthViewModel.AuthUiState.Loading) {
                btnLogin.setEnabled(false);
            }
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass  = etPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Συμπληρώστε όλα τα πεδία", Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.login(email, pass);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContrastHelper.applyCurrentContrast(this);
    }
}
