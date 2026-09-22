package com.example.ekavpraxis.data;

import java.util.Map;

public class StudentForm {

    private String              id;
    private String              studentUid;
    private String              studentName;
    private String              type;
    private Map<String, Object> content;
    private boolean             scanned;
    private long                createdAt;

    // Constructor άδειος — για Firestore
    public StudentForm() {}

    public StudentForm(String id, String studentUid, String studentName,
                       String type, Map<String, Object> content,
                       boolean scanned, long createdAt) {
        this.id          = id;
        this.studentUid  = studentUid;
        this.studentName = studentName;
        this.type        = type;
        this.content     = content;
        this.scanned     = scanned;
        this.createdAt   = createdAt;
    }

    // Getters
    public String              getId()          { return id; }
    public String              getStudentUid()  { return studentUid; }
    public String              getStudentName() { return studentName; }
    public String              getType()        { return type; }
    public Map<String, Object> getContent()     { return content; }
    public boolean             isScanned()      { return scanned; }
    public long                getCreatedAt()   { return createdAt; }

    // Setters
    public void setId(String id)                          { this.id = id; }
    public void setStudentUid(String studentUid)          { this.studentUid = studentUid; }
    public void setStudentName(String studentName)        { this.studentName = studentName; }
    public void setType(String type)                      { this.type = type; }
    public void setContent(Map<String, Object> content)   { this.content = content; }
    public void setScanned(boolean scanned)               { this.scanned = scanned; }
    public void setCreatedAt(long createdAt)              { this.createdAt = createdAt; }
}
