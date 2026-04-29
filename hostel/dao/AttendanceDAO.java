package com.hostel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hostel.model.Attendance;
import com.hostel.model.AttendanceDetail;
import com.hostel.util.DatabaseConnection;

public class AttendanceDAO {

    public void markAttendance(Attendance a) {
        // Check if attendance already marked for this student on this date
        if (isAttendanceMarked(a.getStudentId(), a.getDate())) {
            updateAttendance(a);
        } else {
            addAttendance(a);
        }
    }

    private boolean isAttendanceMarked(String studentId, String date) {
        String sql = "SELECT id FROM attendance WHERE student_id = ? AND date = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, date);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void addAttendance(Attendance a) {
        String sql = "INSERT INTO attendance(student_id, date, status) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getStudentId());
            pstmt.setString(2, a.getDate());
            pstmt.setString(3, a.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateAttendance(Attendance a) {
        String sql = "UPDATE attendance SET status = ? WHERE student_id = ? AND date = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getStatus());
            pstmt.setString(2, a.getStudentId());
            pstmt.setString(3, a.getDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Attendance> getAttendanceByDate(String date) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE date = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Attendance(
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getString("date"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    
    public int getCountByStatus(String date, String status) {
        String sql = "SELECT COUNT(*) FROM attendance WHERE date = ? AND status = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public List<AttendanceDetail> getAttendanceDetailsByDate(String date) {
        List<AttendanceDetail> list = new ArrayList<>();
        String sql = "SELECT u.id, u.full_name, r.room_number, a.status " +
                     "FROM attendance a " +
                     "JOIN users u ON a.student_id = u.id " +
                     "LEFT JOIN allocations al ON u.id = al.student_id " +
                     "LEFT JOIN rooms r ON al.room_id = r.id " +
                     "WHERE a.date = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String room = rs.getString("room_number");
                if (room == null) room = "N/A";
                
                list.add(new AttendanceDetail(
                    rs.getString("id"),
                    rs.getString("full_name"),
                    room,
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
