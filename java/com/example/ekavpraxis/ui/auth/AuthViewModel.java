package com.example.ekavpraxis.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ekavpraxis.data.AppUser;
import com.example.ekavpraxis.data.AuthRepository;
import com.example.ekavpraxis.data.UserRole;

public class AuthViewModel extends ViewModel {

    private final AuthRepository repository = new AuthRepository();

    // LiveData αντί για StateFlow (πιο οικείο σε Java)
    private final MutableLiveData<AuthUiState> uiState = new MutableLiveData<>(new AuthUiState.Idle());
    public LiveData<AuthUiState> getUiState() { return uiState; }

    public static abstract class AuthUiState {
        public static class Idle    extends AuthUiState {}
        public static class Loading extends AuthUiState {}
        public static class Success extends AuthUiState {
            public final AppUser user;
            public Success(AppUser user) { this.user = user; }
        }
        public static class Error extends AuthUiState {
            public final String message;
            public Error(String message) { this.message = message; }
        }
    }

    public void checkExistingSession() {
        uiState.setValue(new AuthUiState.Loading());
        repository.getCurrentUser(new AuthRepository.AuthCallback() {
            @Override public void onSuccess(AppUser user) {
                uiState.setValue(new AuthUiState.Success(user));
            }
            @Override public void onError(String message) {
                uiState.setValue(new AuthUiState.Idle());
            }
        });
    }

    public void login(String email, String password) {
        uiState.setValue(new AuthUiState.Loading());
        repository.login(email, password, new AuthRepository.AuthCallback() {
            @Override public void onSuccess(AppUser user) {
                uiState.setValue(new AuthUiState.Success(user));
            }
            @Override public void onError(String message) {
                uiState.setValue(new AuthUiState.Error(message));
            }
        });
    }

    public void register(String email, String password, String name, UserRole role) {
        uiState.setValue(new AuthUiState.Loading());
        repository.register(email, password, name, role, new AuthRepository.AuthCallback() {
            @Override public void onSuccess(AppUser user) {
                uiState.setValue(new AuthUiState.Success(user));
            }
            @Override public void onError(String message) {
                uiState.setValue(new AuthUiState.Error(message));
            }
        });
    }

    public void logout() {
        repository.logout();
        uiState.setValue(new AuthUiState.Idle());
    }
}
