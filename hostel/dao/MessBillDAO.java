package com.hostel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hostel.model.MessBill;
import com.hostel.util.DatabaseConnection;

public class MessBillDAO {

    public void addBill(MessBill bill) {
        String sql = "INSERT INTO mess_bills(student_id, amount, month, year, status, generated_date) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bill.getStudentId());
            pstmt.setDouble(2, bill.getAmount());
            pstmt.setString(3, bill.getMonth());
            pstmt.setInt(4, bill.getYear());
            pstmt.setString(5, bill.getStatus());
            pstmt.setString(6, bill.getGeneratedDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateBill(MessBill bill) {
        String sql = "UPDATE mess_bills SET student_id = ?, amount = ?, month = ?, year = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bill.getStudentId());
            pstmt.setDouble(2, bill.getAmount());
            pstmt.setString(3, bill.getMonth());
            pstmt.setInt(4, bill.getYear());
            pstmt.setString(5, bill.getStatus());
            pstmt.setInt(6, bill.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<MessBill> getBillsByStudent(String studentId) {
        List<MessBill> list = new ArrayList<>();
        String sql = "SELECT * FROM mess_bills WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MessBill(
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getDouble("amount"),
                    rs.getString("month"),
                    rs.getInt("year"),
                    rs.getString("status"),
                    rs.getString("generated_date")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<MessBill> getAllBills() {
        List<MessBill> list = new ArrayList<>();
        String sql = "SELECT * FROM mess_bills";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new MessBill(
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getDouble("amount"),
                    rs.getString("month"),
                    rs.getInt("year"),
                    rs.getString("status"),
                    rs.getString("generated_date")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
