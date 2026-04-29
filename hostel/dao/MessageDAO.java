package com.hostel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hostel.model.Message;
import com.hostel.util.DatabaseConnection;

public class MessageDAO {

    public void sendMessage(Message m) {
        String sql = "INSERT INTO messages(sender_id, receiver_id, content, timestamp) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, m.getSenderId());
            pstmt.setString(2, m.getReceiverId());
            pstmt.setString(3, m.getContent());
            pstmt.setString(4, m.getTimestamp());
            int rows = pstmt.executeUpdate();
            System.out.println("Message sent! Rows affected: " + rows);
        } catch (SQLException e) {
            System.out.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Message> getMessagesForUser(String userId) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT m.id, m.sender_id, m.receiver_id, m.content, m.timestamp, u.full_name as sender_name " +
                     "FROM messages m " +
                     "JOIN users u ON m.sender_id = u.id " +
                     "WHERE m.receiver_id = ? OR m.sender_id = ? " +
                     "ORDER BY m.timestamp DESC";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Message(
                    rs.getInt("id"),
                    rs.getString("sender_id"),
                    rs.getString("receiver_id"),
                    rs.getString("content"),
                    rs.getString("timestamp"),
                    rs.getString("sender_name")
                ));
            }
            System.out.println("Fetched " + list.size() + " messages for user: " + userId);
        } catch (SQLException e) {
            System.out.println("Error fetching messages: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Message> getConversation(String user1, String user2) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT m.id, m.sender_id, m.receiver_id, m.content, m.timestamp, u.full_name as sender_name " +
                     "FROM messages m " +
                     "JOIN users u ON m.sender_id = u.id " +
                     "WHERE (m.sender_id = ? AND m.receiver_id = ?) OR (m.sender_id = ? AND m.receiver_id = ?) " +
                     "ORDER BY m.timestamp ASC";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user1);
            pstmt.setString(2, user2);
            pstmt.setString(3, user2);
            pstmt.setString(4, user1);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Message(
                    rs.getInt("id"),
                    rs.getString("sender_id"),
                    rs.getString("receiver_id"),
                    rs.getString("content"),
                    rs.getString("timestamp"),
                    rs.getString("sender_name")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
