package com.hostel.view;

import com.hostel.dao.UserDAO;
import com.hostel.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {

    private UserDAO userDAO = new UserDAO();

    public void show(Stage stage) {
        stage.setTitle("Smart Hostel Management - Login");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        // Enhanced background with gradient
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #6a11cb, #2575fc);");

        Label titleLabel = new Label("Smart Hostel Login");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-padding: 30; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 20, 0, 0, 0);");

        Label userLabel = new Label("Username:");
        userLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        TextField userTextField = new TextField();
        userTextField.setPromptText("Enter username");
        userTextField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        
        Label pwLabel = new Label("Password:");
        pwLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("Enter password");
        pwBox.setStyle("-fx-background-radius: 5; -fx-padding: 8;");

        grid.add(userLabel, 0, 0);
        grid.add(userTextField, 1, 0);
        grid.add(pwLabel, 0, 1);
        grid.add(pwBox, 1, 1);

        Button btn = new Button("Sign In");
        btn.setStyle("-fx-background-color: #0984e3; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 10 30;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #00cec9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 10 30;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #0984e3; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 10 30;"));
        
        Label messageLabel = new Label();
        messageLabel.setTextFill(javafx.scene.paint.Color.YELLOW);
        messageLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        btn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter credentials.");
                return;
            }

            User user = userDAO.authenticate(username, password);
            if (user != null) {
                messageLabel.setText("Login Successful!");
                messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                // Proceed to Dashboard
                new DashboardView().show(stage, user);
            } else {
                messageLabel.setText("Invalid username or password.");
                messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            }
        });

        root.getChildren().addAll(titleLabel, grid, btn, messageLabel);

        Scene scene = new Scene(root, 400, 350);
        stage.setScene(scene);
        stage.show();
    }
}
