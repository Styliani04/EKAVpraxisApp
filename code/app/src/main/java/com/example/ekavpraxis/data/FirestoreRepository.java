package com.example.ekavpraxis.data;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface AmbulanceCallback {
        void onSuccess(List<Ambulance> list);
        void onError(String message);
    }

    public interface ScheduleCallback {
        void onSuccess(List<ScheduleEvent> list);
        void onError(String message);
    }

    public interface StudentsCallback {
        void onSuccess(List<Map<String, Object>> list);
        void onError(String message);
    }
    public void saveAmbulance(Ambulance ambulance, SimpleCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("name",      ambulance.getName());
        data.put("plate",     ambulance.getPlate());
        data.put("lat",       ambulance.getLat());
        data.put("lng",       ambulance.getLng());
        data.put("available", ambulance.isAvailable());
        data.put("hospital",  ambulance.getHospital());
        data.put("updatedAt", System.currentTimeMillis());

        if (ambulance.getId().isEmpty()) {
            db.collection("ambulances").add(data)
                .addOnSuccessListener(r -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
        } else {
            db.collection("ambulances").document(ambulance.getId()).set(data)
                .addOnSuccessListener(r -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
        }
    }

    public void deleteAmbulance(String ambulanceId, SimpleCallback callback) {
        db.collection("ambulances").document(ambulanceId).delete()
            .addOnSuccessListener(r -> callback.onSuccess())
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getAvailableAmbulances(AmbulanceCallback callback) {
        db.collection("ambulances")
            .whereEqualTo("available", true)
            .get()
            .addOnSuccessListener(snapshot -> {
                List<Ambulance> list = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshot) {
                    Ambulance a = new Ambulance(
                        doc.getId(),
                        doc.getString("name")    != null ? doc.getString("name")    : "",
                        doc.getString("plate")   != null ? doc.getString("plate")   : "",
                        doc.getDouble("lat")     != null ? doc.getDouble("lat")     : 0.0,
                        doc.getDouble("lng")     != null ? doc.getDouble("lng")     : 0.0,
                        doc.getBoolean("available") != null ? doc.getBoolean("available") : true,
                        doc.getString("hospital") != null ? doc.getString("hospital") : "",
                        doc.getLong("updatedAt") != null ? doc.getLong("updatedAt") : 0L
                    );
                    list.add(a);
                }
                callback.onSuccess(list);
            })
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void addScheduleEvent(String studentUid, ScheduleEvent event,
                                  SimpleCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("title",    event.getTitle());
        data.put("date",     event.getDate());
        data.put("location", event.getLocation());
        data.put("shift",    event.getShift());

        db.collection("users").document(studentUid)
            .collection("schedule").add(data)
            .addOnSuccessListener(r -> callback.onSuccess())
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getSchedule(String studentUid, ScheduleCallback callback) {
        db.collection("users").document(studentUid)
            .collection("schedule")
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener(snapshot -> {
                List<ScheduleEvent> list = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshot) {
                    list.add(new ScheduleEvent(
                        doc.getId(),
                        doc.getString("title")    != null ? doc.getString("title")    : "",
                        doc.getString("date")     != null ? doc.getString("date")     : "",
                        doc.getString("location") != null ? doc.getString("location") : "",
                        doc.getString("shift")    != null ? doc.getString("shift")    : ""
                    ));
                }
                callback.onSuccess(list);
            })
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getAllStudents(StudentsCallback callback) {
        db.collection("users")
            .whereEqualTo("role", "STUDENT")
            .get()
            .addOnSuccessListener(snapshot -> {
                List<Map<String, Object>> list = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshot) {
                    if (doc.getData() != null) list.add(doc.getData());
                }
                callback.onSuccess(list);
            })
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void saveForm(StudentForm form, SimpleCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("studentUid",  form.getStudentUid());
        data.put("studentName", form.getStudentName());
        data.put("type",        form.getType());
        data.put("content",     form.getContent());
        data.put("scanned",     form.isScanned());
        data.put("createdAt",   System.currentTimeMillis());

        db.collection("forms").add(data)
                .addOnSuccessListener(r -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}
