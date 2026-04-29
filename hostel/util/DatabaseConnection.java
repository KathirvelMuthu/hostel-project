package com.hostel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:hostel_management.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void initializeDatabase() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id TEXT PRIMARY KEY,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL,"
                + "role TEXT NOT NULL,"
                + "full_name TEXT,"
                + "contact TEXT,"
                + "department TEXT"
                + ");";

        String roomsTable = "CREATE TABLE IF NOT EXISTS rooms ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "room_number TEXT NOT NULL UNIQUE,"
                + "type TEXT NOT NULL,"
                + "capacity INTEGER NOT NULL,"
                + "current_occupancy INTEGER DEFAULT 0"
                + ");";

        String allocationsTable = "CREATE TABLE IF NOT EXISTS allocations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "student_id TEXT,"
                + "room_id INTEGER,"
                + "allocation_date TEXT,"
                + "FOREIGN KEY(student_id) REFERENCES users(id),"
                + "FOREIGN KEY(room_id) REFERENCES rooms(id)"
                + ");";

        String complaintsTable = "CREATE TABLE IF NOT EXISTS complaints ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "student_id TEXT,"
                + "description TEXT,"
                + "status TEXT DEFAULT 'Pending',"
                + "date_logged TEXT,"
                + "remarks TEXT,"
                + "FOREIGN KEY(student_id) REFERENCES users(id)"
                + ");";

        String messBillsTable = "CREATE TABLE IF NOT EXISTS mess_bills ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "student_id TEXT,"
                + "amount REAL,"
                + "month TEXT,"
                + "year INTEGER,"
                + "status TEXT DEFAULT 'Unpaid',"
                + "generated_date TEXT,"
                + "FOREIGN KEY(student_id) REFERENCES users(id)"
                + ");";

        String permissionsTable = "CREATE TABLE IF NOT EXISTS permissions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "student_id TEXT,"
                + "type TEXT,"
                + "start_date TEXT,"
                + "end_date TEXT,"
                + "start_time TEXT,"
                + "end_time TEXT,"
                + "reason TEXT,"
                + "status TEXT DEFAULT 'Pending',"
                + "FOREIGN KEY(student_id) REFERENCES users(id)"
                + ");";

        String attendanceTable = "CREATE TABLE IF NOT EXISTS attendance ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "student_id TEXT,"
                + "date TEXT,"
                + "status TEXT,"
                + "FOREIGN KEY(student_id) REFERENCES users(id)"
                + ");";

        String messagesTable = "CREATE TABLE IF NOT EXISTS messages ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "sender_id TEXT,"
                + "receiver_id TEXT,"
                + "content TEXT,"
                + "timestamp TEXT,"
                + "FOREIGN KEY(sender_id) REFERENCES users(id),"
                + "FOREIGN KEY(receiver_id) REFERENCES users(id)"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(roomsTable);
            stmt.execute(allocationsTable);
            stmt.execute(complaintsTable);
            stmt.execute(messBillsTable);
            stmt.execute(permissionsTable);
            stmt.execute(attendanceTable);
            stmt.execute(messagesTable);
            
            // Insert default admin/warden if not exists
            // Note: The ID is not auto-increment for users table (TEXT PRIMARY KEY), so we must provide it or change schema.
            // Wait, the schema says "id TEXT PRIMARY KEY".
            // The INSERT OR IGNORE statements below rely on 'username' being UNIQUE, but they don't provide 'id'.
            // If 'id' is NOT NULL (implied by PRIMARY KEY), this might fail or insert NULL if SQLite allows (it shouldn't for PK).
            // Actually, for TEXT PK, we should provide it.
            // Let's fix the seed data to include IDs.
            
            stmt.execute("INSERT OR IGNORE INTO users (id, username, password, role, full_name) VALUES ('W001', 'admin', 'admin123', 'Warden', 'Chief Warden');");
            stmt.execute("INSERT OR IGNORE INTO users (id, username, password, role, full_name) VALUES ('S001', 'student', 'student123', 'Student', 'John Doe');");
            stmt.execute("INSERT OR IGNORE INTO users (id, username, password, role, full_name) VALUES ('ST001', 'staff', 'staff123', 'Staff', 'Bob Builder');");
            
            System.out.println("Database initialized.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
