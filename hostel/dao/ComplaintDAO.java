package com.hostel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hostel.model.Complaint;
import com.hostel.util.DatabaseConnection;

public class ComplaintDAO {

    public boolean addComplaint(Complaint complaint) {
        String sql = "INSERT INTO complaints(student_id, description, status, date_logged) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, complaint.getStudentId());
            pstmt.setString(2, complaint.getDescription());
            pstmt.setString(3, complaint.getStatus());
            pstmt.setString(4, complaint.getDateLogged());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<Complaint> getComplaintsByStudent(String studentId) {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Complaint(
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getString("date_logged"),
                    rs.getString("remarks")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Complaint> getAllComplaints() {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Complaint(
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getString("date_logged"),
                    rs.getString("remarks")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public void updateComplaint(Complaint complaint) {
        String sql = "UPDATE complaints SET status = ?, remarks = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, complaint.getStatus());
            pstmt.setString(2, complaint.getRemarks());
            pstmt.setInt(3, complaint.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
