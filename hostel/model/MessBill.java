package com.hostel.model;

public class MessBill {
    private int id;
    private String studentId;
    private double amount;
    private String month;
    private int year;
    private String status;
    private String generatedDate;

    public MessBill() {}

    public MessBill(int id, String studentId, double amount, String month, int year, String status, String generatedDate) {
        this.id = id;
        this.studentId = studentId;
        this.amount = amount;
        this.month = month;
        this.year = year;
        this.status = status;
        this.generatedDate = generatedDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(String generatedDate) { this.generatedDate = generatedDate; }
}
