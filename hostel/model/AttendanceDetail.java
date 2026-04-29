package com.hostel.model;

public class AttendanceDetail {
    private String studentId;
    private String studentName;
    private String roomNumber;
    private String status;

    public AttendanceDetail(String studentId, String studentName, String roomNumber, String status) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
