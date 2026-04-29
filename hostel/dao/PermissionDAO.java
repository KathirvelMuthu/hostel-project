package com.hostel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hostel.model.Permission;
import com.hostel.util.DatabaseConnection;

public class PermissionDAO {

    public void addPermission(Permission p) {
        String sql = "INSERT INTO permissions(student_id, type, start_date, end_date, start_time, end_time, reason, status) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getStudentId());
            pstmt.setString(2, p.getType());
            pstmt.setString(3, p.getStartDate());
            pstmt.setString(4, p.getEndDate());
            pstmt.setString(5, p.getStartTime());
            pstmt.setString(6, p.getEndTime());
            pstmt.setString(7, p.getReason());
            pstmt.setString(8, p.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Permission> getPermissionsByStudent(String studentId) {
        List<Permission> list = new ArrayList<>();
        String sql = "SELECT * FROM permissions WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Permission(
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getString("type"),
                    rs.getString("start_date"),
                    rs.getString("end_date"),
                    rs.getString("start_time"),
                    rs.getString("end_time"),
                    rs.getString("reason"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Permission> getAllPermissions() {
        List<Permission> list = new ArrayList<>();
        String sql = "SELECT * FROM permissions";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Permission(
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getString("type"),
                    rs.getString("start_date"),
                    rs.getString("end_date"),
                    rs.getString("start_time"),
                    rs.getString("end_time"),
                    rs.getString("reason"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public void updatePermissionStatus(int id, String status) {
        String sql = "UPDATE permissions SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
