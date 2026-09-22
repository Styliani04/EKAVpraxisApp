package com.example.ekavpraxis.data;

public class AppUser {
    private String uid;
    private String email;
    private UserRole role;

    public AppUser(String uid, String email, UserRole role) {
        this.uid   = uid;
        this.email = email;
        this.role  = role;
    }

    public String   getUid()   { return uid; }
    public String   getEmail() { return email; }
    public UserRole getRole()  { return role; }
}
