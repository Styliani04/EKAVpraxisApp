package com.example.ekavpraxis.data;

public class Ambulance {

    private String  id;
    private String  name;
    private String  plate;
    private double  lat;
    private double  lng;
    private boolean available;
    private String  hospital;
    private long    updatedAt;

    // Constructor άδειος — χρειάζεται για το Firestore
    public Ambulance() {}

    // Constructor με όλα τα fields
    public Ambulance(String id, String name, String plate,
                     double lat, double lng,
                     boolean available, String hospital, long updatedAt) {
        this.id        = id;
        this.name      = name;
        this.plate     = plate;
        this.lat       = lat;
        this.lng       = lng;
        this.available = available;
        this.hospital  = hospital;
        this.updatedAt = updatedAt;
    }

    // Getters
    public String  getId()        { return id; }
    public String  getName()      { return name; }
    public String  getPlate()     { return plate; }
    public double  getLat()       { return lat; }
    public double  getLng()       { return lng; }
    public boolean isAvailable()  { return available; }
    public String  getHospital()  { return hospital; }
    public long    getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(String id)               { this.id = id; }
    public void setName(String name)           { this.name = name; }
    public void setPlate(String plate)         { this.plate = plate; }
    public void setLat(double lat)             { this.lat = lat; }
    public void setLng(double lng)             { this.lng = lng; }
    public void setAvailable(boolean available){ this.available = available; }
    public void setHospital(String hospital)   { this.hospital = hospital; }
    public void setUpdatedAt(long updatedAt)   { this.updatedAt = updatedAt; }
}
