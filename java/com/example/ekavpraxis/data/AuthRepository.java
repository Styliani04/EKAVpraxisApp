package com.example.ekavpraxis.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthRepository {

    private final FirebaseAuth      auth;
    private final FirebaseFirestore db;

    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();
    }

    public interface AuthCallback {
        void onSuccess(AppUser user);
        void onError(String message);
    }

    public void register(String email, String password,
                         String name, UserRole role,
                         AuthCallback callback) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
                String uid = authResult.getUser().getUid();

                // Αποθήκευση δεδομένων στο Firestore
                Map<String, Object> userDoc = new HashMap<>();
                userDoc.put("uid",       uid);
                userDoc.put("email",     email);
                userDoc.put("name",      name);
                userDoc.put("role",      role.name());   // "STUDENT" ή "SECRETARY"
                userDoc.put("createdAt", System.currentTimeMillis());

                db.collection("users").document(uid)
                    .set(userDoc)
                    .addOnSuccessListener(unused ->
                        callback.onSuccess(new AppUser(uid, email, role))
                    )
                    .addOnFailureListener(e ->
                        callback.onError(e.getMessage())
                    );
            })
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void login(String email, String password, AuthCallback callback) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult -> {
                String uid = authResult.getUser().getUid();

                // Διάβασμα role από Firestore
                db.collection("users").document(uid).get()
                    .addOnSuccessListener(doc -> {
                        String roleStr = doc.getString("role");
                        UserRole role;
                        try {
                            role = UserRole.valueOf(roleStr);
                        } catch (Exception e) {
                            role = UserRole.UNKNOWN;
                        }
                        callback.onSuccess(new AppUser(uid, email, role));
                    })
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
            })
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void logout() {
        auth.signOut();
    }

    public void getCurrentUser(AuthCallback callback) {
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser == null) {
            callback.onError("No session");
            return;
        }

        db.collection("users").document(firebaseUser.getUid()).get()
            .addOnSuccessListener(doc -> {
                String roleStr = doc.getString("role");
                UserRole role;
                try {
                    role = UserRole.valueOf(roleStr);
                } catch (Exception e) {
                    role = UserRole.UNKNOWN;
                }
                callback.onSuccess(
                    new AppUser(firebaseUser.getUid(), firebaseUser.getEmail(), role)
                );
            })
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}
