package com.hostel;

import com.hostel.util.DatabaseConnection;
import com.hostel.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize Database
        DatabaseConnection.initializeDatabase();

        // Show Login View
        LoginView loginView = new LoginView();
        loginView.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
