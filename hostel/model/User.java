package com.hostel.model;

public class User {
    private String id;
    private String username;
    private String password;
    private String role;
    private String fullName;
    private String contact;
    private String department;
    private String roomNumber; // Transient field for display

    public User() {}

    public User(String id, String username, String password, String role, String fullName, String contact) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.contact = contact;
    }

    // Constructor with roomNumber
    public User(String id, String username, String password, String role, String fullName, String contact, String roomNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.contact = contact;
        this.roomNumber = roomNumber;
    }

    public User(String id, String username, String password, String role, String fullName, String contact, String department, String roomNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.contact = contact;
        this.department = department;
        this.roomNumber = roomNumber;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}
