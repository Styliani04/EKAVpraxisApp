package com.example.ekavpraxis.data;

public class ScheduleEvent {

    private String id;
    private String title;
    private String date;      // μορφή "2024-03-15"
    private String location;
    private String shift;     // "Πρωινή Βάρδια" ή "Απογευματινή Βάρδια"

    // Constructor άδειος — χρειάζεται για το Firestore
    public ScheduleEvent() {}

    // Constructor με όλα τα fields
    public ScheduleEvent(String id, String title, String date, String location, String shift) {
        this.id       = id;
        this.title    = title;
        this.date     = date;
        this.location = location;
        this.shift    = shift;
    }

    // Getters
    public String getId()       { return id; }
    public String getTitle()    { return title; }
    public String getDate()     { return date; }
    public String getLocation() { return location; }
    public String getShift()    { return shift; }

    // Setters
    public void setId(String id)             { this.id = id; }
    public void setTitle(String title)       { this.title = title; }
    public void setDate(String date)         { this.date = date; }
    public void setLocation(String location) { this.location = location; }
    public void setShift(String shift)       { this.shift = shift; }
}
