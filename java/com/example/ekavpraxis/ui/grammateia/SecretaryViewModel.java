package com.example.ekavpraxis.ui.grammateia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ekavpraxis.data.Ambulance;
import com.example.ekavpraxis.data.FirestoreRepository;

import java.util.List;
import java.util.Map;

public class SecretaryViewModel extends ViewModel {

    private final FirestoreRepository repository = new FirestoreRepository();

    // ── LiveData που παρακολουθούν τα Fragments ────────────────────────────
    private final MutableLiveData<List<Ambulance>>          ambulances    = new MutableLiveData<>();
    private final MutableLiveData<List<Map<String, Object>>> students     = new MutableLiveData<>();
    private final MutableLiveData<String>                    errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean>                   loading      = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean>                   saved        = new MutableLiveData<>(false);

    public LiveData<List<Ambulance>>           getAmbulances()   { return ambulances; }
    public LiveData<List<Map<String, Object>>> getStudents()     { return students; }
    public LiveData<String>                    getErrorMessage() { return errorMessage; }
    public LiveData<Boolean>                   isLoading()       { return loading; }
    public LiveData<Boolean>                   isSaved()         { return saved; }

    // ── ΑΣΘΕΝΟΦΟΡΑ: φόρτωσε τη λίστα ─────────────────────────────────────
    // Κάλεσε το από το AmbulanceFragment
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

    // ── ΑΣΘΕΝΟΦΟΡΑ: πρόσθεσε ή ενημέρωσε ασθενοφόρο ─────────────────────
    // Κάλεσε το από το AmbulanceFragment όταν η γραμματεία ανεβάζει λίστα
    public void saveAmbulance(Ambulance ambulance) {
        loading.setValue(true);
        repository.saveAmbulance(ambulance, new FirestoreRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                saved.setValue(true);
                loading.setValue(false);
                loadAmbulances(); // ανανέωσε τη λίστα
            }
            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                loading.setValue(false);
            }
        });
    }

    // ── ΑΣΘΕΝΟΦΟΡΑ: διέγραψε ασθενοφόρο ──────────────────────────────────
    public void deleteAmbulance(String ambulanceId) {
        repository.deleteAmbulance(ambulanceId, new FirestoreRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                loadAmbulances(); // ανανέωσε τη λίστα
            }
            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
            }
        });
    }

    // ── ΣΠΟΥΔΑΣΤΕΣ: φόρτωσε καρτέλα εγγεγραμμένων ────────────────────────
    // Κάλεσε το από το StudentsFragment
    public void loadStudents() {
        loading.setValue(true);
        repository.getAllStudents(new FirestoreRepository.StudentsCallback() {
            @Override
            public void onSuccess(List<Map<String, Object>> list) {
                students.setValue(list);
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
