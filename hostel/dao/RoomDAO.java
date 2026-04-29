package com.hostel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hostel.model.Room;
import com.hostel.util.DatabaseConnection;

public class RoomDAO {

    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = DatabaseConnection.connect();
             java.sql.Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getString("type"),
                    rs.getInt("capacity"),
                    rs.getInt("current_occupancy")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public void addRoom(Room room) {
        String sql = "INSERT INTO rooms(room_number, type, capacity, current_occupancy) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getType());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setInt(4, room.getCurrentOccupancy());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateRoom(Room room) {
        String sql = "UPDATE rooms SET room_number = ?, type = ?, capacity = ?, current_occupancy = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getType());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setInt(4, room.getCurrentOccupancy());
            pstmt.setInt(5, room.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Integer getRoomId(String roomNumber) {
        String sql = "SELECT id FROM rooms WHERE room_number = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void allocateRoom(String studentId, String roomNumber) {
        Integer roomId = getRoomId(roomNumber);
        if (roomId == null) {
            // Create room if not exists for simplicity or throw error
            // For this requirement, let's auto-create the room if it doesn't exist to make data entry easier
            createRoom(roomNumber, "Single", 1); 
            roomId = getRoomId(roomNumber);
        }

        // Remove existing allocation if any
        deallocateRoom(studentId);

        String sql = "INSERT INTO allocations(student_id, room_id, allocation_date) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, roomId);
            pstmt.setString(3, java.time.LocalDate.now().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deallocateRoom(String studentId) {
        String sql = "DELETE FROM allocations WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createRoom(String roomNumber, String type, int capacity) {
        String sql = "INSERT INTO rooms(room_number, type, capacity) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            pstmt.setString(2, type);
            pstmt.setInt(3, capacity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
