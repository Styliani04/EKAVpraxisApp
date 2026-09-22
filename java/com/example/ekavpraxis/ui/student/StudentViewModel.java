package com.example.ekavpraxis.ui.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ekavpraxis.data.Ambulance;
import com.example.ekavpraxis.data.FirestoreRepository;
import com.example.ekavpraxis.data.ScheduleEvent;
import com.example.ekavpraxis.data.StudentForm;

import java.util.List;

public class StudentViewModel extends ViewModel {

    private final FirestoreRepository repository = new FirestoreRepository();

    // ── LiveData που παρακολουθούν τα Fragments ────────────────────────────
    private final MutableLiveData<List<Ambulance>>     ambulances   = new MutableLiveData<>();
    private final MutableLiveData<List<ScheduleEvent>> schedule     = new MutableLiveData<>();
    private final MutableLiveData<String>              errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean>             loading      = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean>             formSaved    = new MutableLiveData<>(false);

    public LiveData<List<Ambulance>>     getAmbulances()   { return ambulances; }
    public LiveData<List<ScheduleEvent>> getSchedule()     { return schedule; }
    public LiveData<String>              getErrorMessage() { return errorMessage; }
    public LiveData<Boolean>             isLoading()       { return loading; }
    public LiveData<Boolean>             isFormSaved()     { return formSaved; }

    // ── ΧΑΡΤΗΣ: φόρτωσε διαθέσιμα ασθενοφόρα ─────────────────────────────
    // Κάλεσε το από το MapFragment
    public void loadAmbulances() {
        loading.setValue(true);
        repository.getAvailableAmbulances(new FirestoreRepository.AmbulanceCallback() {
            @Override
            public void onSuccess(List<Ambulance> list) {
                ambulances.setValue(list);
                loading.setValue(false);
            }
            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                loading.setValue(false);
            }
        });
    }

    // ── ΗΜΕΡΟΛΟΓΙΟ: φόρτωσε πρόγραμμα σπουδαστή ──────────────────────────
    // Κάλεσε το από το ScheduleFragment
    public void loadSchedule(String studentUid) {
        loading.setValue(true);
        repository.getSchedule(studentUid, new FirestoreRepository.ScheduleCallback() {
            @Override
            public void onSuccess(List<ScheduleEvent> list) {
                schedule.setValue(list);
                loading.setValue(false);
            }
            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                loading.setValue(false);
            }
        });
    }

    // ── ΗΜΕΡΟΛΟΓΙΟ: πρόσθεσε event στο πρόγραμμα ─────────────────────────
    // Κάλεσε το από το ScheduleFragment όταν ο σπουδαστής δηλώνει πρόγραμμα
    public void addScheduleEvent(String studentUid, ScheduleEvent event) {
        repository.addScheduleEvent(studentUid, event, new FirestoreRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                // Ξαναφόρτωσε το πρόγραμμα για να ενημερωθεί το ημερολόγιο
                loadSchedule(studentUid);
            }
            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
            }
        });
    }

    // ── ΦΟΡΜΑ: αποθήκευσε φόρμα (manual ή OCR) ───────────────────────────
    // Κάλεσε το από το FormFragment
    public void saveForm(StudentForm form) {
        loading.setValue(true);
        repository.saveForm(form, new FirestoreRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                formSaved.setValue(true);
                loading.setValue(false);
            }
            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                loading.setValue(false);
            }
        });
    }
}
