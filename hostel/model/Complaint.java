package com.hostel.model;

public class Complaint {
    private int id;
    private String studentId;
    private String description;
    private String status;
    private String dateLogged;
    private String remarks;

    public Complaint() {}

    public Complaint(int id, String studentId, String description, String status, String dateLogged) {
        this.id = id;
        this.studentId = studentId;
        this.description = description;
        this.status = status;
        this.dateLogged = dateLogged;
    }

    public Complaint(int id, String studentId, String description, String status, String dateLogged, String remarks) {
        this.id = id;
        this.studentId = studentId;
        this.description = description;
        this.status = status;
        this.dateLogged = dateLogged;
        this.remarks = remarks;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDateLogged() { return dateLogged; }
    public void setDateLogged(String dateLogged) { this.dateLogged = dateLogged; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
