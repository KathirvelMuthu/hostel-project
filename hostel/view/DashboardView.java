package com.hostel.view;

import java.time.LocalDate;
import java.util.List;

import com.hostel.dao.AttendanceDAO;
import com.hostel.dao.ComplaintDAO;
import com.hostel.dao.MessBillDAO;
import com.hostel.dao.MessageDAO;
import com.hostel.dao.PermissionDAO;
import com.hostel.dao.RoomDAO;
import com.hostel.dao.UserDAO;
import com.hostel.model.Attendance;
import com.hostel.model.AttendanceDetail;
import com.hostel.model.Complaint;
import com.hostel.model.MessBill;
import com.hostel.model.Message;
import com.hostel.model.Permission;
import com.hostel.model.Room;
import com.hostel.model.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardView {

    private ComplaintDAO complaintDAO = new ComplaintDAO();
    private UserDAO userDAO = new UserDAO();
    private RoomDAO roomDAO = new RoomDAO();
    private MessBillDAO messBillDAO = new MessBillDAO();
    private PermissionDAO permissionDAO = new PermissionDAO();
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private MessageDAO messageDAO = new MessageDAO();

    public void show(Stage stage, User user) {
        stage.setTitle("Smart Hostel Management - " + user.getRole() + " Dashboard");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f2f5;"); // Light gray background for the whole app
        
        // Top Bar with Gradient
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15, 25, 15, 25));
        topBar.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #4ca1af); -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0, 0, 0, 5);");
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Label welcomeLabel = new Label("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
        welcomeLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        welcomeLabel.setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.5), 2, 0, 0, 0);");
        
        HBox spacer = new HBox();
        javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 20;");
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 20;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 8 20;"));
        logoutBtn.setOnAction(e -> new LoginView().show(stage));
        
        topBar.getChildren().addAll(welcomeLabel, spacer, logoutBtn);
        root.setTop(topBar);

        // Center Content with Tabs
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent; -fx-tab-min-height: 35;");
        
        // Home Tab
        Tab homeTab = new Tab("Home");
        homeTab.setClosable(false);
        homeTab.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        homeTab.setContent(createHomeView(user));
        tabPane.getTabs().add(homeTab);

        // Student Management Tab (Warden Only)
        if ("Warden".equalsIgnoreCase(user.getRole())) {
            Tab studentTab = new Tab("Manage Students");
            studentTab.setClosable(false);
            studentTab.setContent(createStudentManagementView());
            tabPane.getTabs().add(studentTab);
        }

        // Room Management Tab (Warden & Staff)
        if ("Warden".equalsIgnoreCase(user.getRole()) || "Staff".equalsIgnoreCase(user.getRole()) || "Maintenance".equalsIgnoreCase(user.getRole())) {
            Tab roomTab = new Tab("Manage Rooms");
            roomTab.setClosable(false);
            roomTab.setContent(createRoomManagementView());
            tabPane.getTabs().add(roomTab);
        }

        // Complaints Tab
        Tab complaintsTab = new Tab("Complaints");
        complaintsTab.setClosable(false);
        complaintsTab.setContent(createComplaintView(user));
        tabPane.getTabs().add(complaintsTab);

        // Mess Billing Tab (Warden & Staff)
        if ("Warden".equalsIgnoreCase(user.getRole()) || "Staff".equalsIgnoreCase(user.getRole())) {
            Tab messBillingTab = new Tab("Mess Billing");
            messBillingTab.setClosable(false);
            messBillingTab.setContent(createMessBillingView());
            tabPane.getTabs().add(messBillingTab);
        } else if ("Student".equalsIgnoreCase(user.getRole())) {
            // Student view for their own mess bills
            Tab myBillsTab = new Tab("My Mess Bills");
            myBillsTab.setClosable(false);
            myBillsTab.setContent(createStudentMessBillView(user));
            tabPane.getTabs().add(myBillsTab);
        }

        // Permission Tab (Student & Warden)
        if ("Student".equalsIgnoreCase(user.getRole())) {
            Tab permissionTab = new Tab("Permissions");
            permissionTab.setClosable(false);
            permissionTab.setContent(createStudentPermissionView(user));
            tabPane.getTabs().add(permissionTab);
        } else if ("Warden".equalsIgnoreCase(user.getRole())) {
            Tab permissionTab = new Tab("Manage Permissions");
            permissionTab.setClosable(false);
            permissionTab.setContent(createWardenPermissionView());
            tabPane.getTabs().add(permissionTab);
        }

        // Attendance Tab (Warden & Staff)
        if ("Warden".equalsIgnoreCase(user.getRole()) || "Staff".equalsIgnoreCase(user.getRole())) {
            Tab attendanceTab = new Tab("Attendance");
            attendanceTab.setClosable(false);
            attendanceTab.setContent(createAttendanceView());
            tabPane.getTabs().add(attendanceTab);
        }

        // Message Tab (Warden/Admin)
        if ("Warden".equalsIgnoreCase(user.getRole()) || "Admin".equalsIgnoreCase(user.getRole())) {
            Tab messageTab = new Tab("Messages");
            messageTab.setClosable(false);
            messageTab.setContent(createWardenMessageView(user));
            tabPane.getTabs().add(messageTab);
        }

        // Message Tab (Student)
        if ("Student".equalsIgnoreCase(user.getRole())) {
            Tab messageTab = new Tab("Messages");
            messageTab.setClosable(false);
            messageTab.setContent(createStudentMessageView(user));
            tabPane.getTabs().add(messageTab);
        }

        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createStudentManagementView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Student Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Table
        TableView<User> table = new TableView<>();
        
        TableColumn<User, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<User, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<User, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        
        TableColumn<User, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        
        TableColumn<User, String> roomCol = new TableColumn<>("Room No");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        table.getColumns().addAll(idCol, nameCol, deptCol, contactCol, roomCol);
        
        refreshStudentTable(table);

        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));
        form.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-padding: 10px;");

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField deptField = new TextField();
        TextField contactField = new TextField();
        TextField roomField = new TextField();
        PasswordField passwordField = new PasswordField();

        form.add(new Label("Student ID:"), 0, 0);
        form.add(idField, 1, 0);
        form.add(new Label("Full Name:"), 2, 0);
        form.add(nameField, 3, 0);
        form.add(new Label("Department:"), 0, 1);
        form.add(deptField, 1, 1);
        form.add(new Label("Contact:"), 2, 1);
        form.add(contactField, 3, 1);
        form.add(new Label("Room No:"), 0, 2);
        form.add(roomField, 1, 2);
        form.add(new Label("Password:"), 2, 2);
        form.add(passwordField, 3, 2);

        HBox buttons = new HBox(10);
        Button addBtn = new Button("Add Student");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button clearBtn = new Button("Clear");

        buttons.getChildren().addAll(addBtn, updateBtn, deleteBtn, clearBtn);

        // Selection Listener
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(newSelection.getId());
                nameField.setText(newSelection.getFullName());
                deptField.setText(newSelection.getDepartment());
                contactField.setText(newSelection.getContact());
                roomField.setText(newSelection.getRoomNumber());
                passwordField.setText(newSelection.getPassword());
                idField.setDisable(true); // Cannot change ID
            }
        });

        // Actions
        addBtn.setOnAction(e -> {
            if (idField.getText().isEmpty() || nameField.getText().isEmpty()) {
                showAlert("Error", "ID and Name are required.");
                return;
            }
            
            User u = new User();
            u.setId(idField.getText());
            u.setFullName(nameField.getText());
            u.setUsername(idField.getText()); // Use ID as username
            u.setPassword(passwordField.getText().isEmpty() ? "123456" : passwordField.getText());
            u.setRole("Student");
            u.setDepartment(deptField.getText());
            u.setContact(contactField.getText());
            
            String newId = userDAO.addUser(u);
            if (newId != null && !roomField.getText().isEmpty()) {
                roomDAO.allocateRoom(newId, roomField.getText());
            }
            refreshStudentTable(table);
            clearForm(idField, nameField, deptField, contactField, roomField, passwordField);
        });

        updateBtn.setOnAction(e -> {
            if (idField.getText().isEmpty() || !idField.isDisabled()) {
                showAlert("Error", "Select a student to update.");
                return;
            }
            User u = new User();
            u.setId(idField.getText());
            u.setFullName(nameField.getText());
            u.setDepartment(deptField.getText());
            u.setContact(contactField.getText());
            // Note: Password update not implemented in DAO update method yet, but could be added.
            
            userDAO.updateUser(u);
            
            if (!roomField.getText().isEmpty()) {
                roomDAO.allocateRoom(u.getId(), roomField.getText());
            } else {
                roomDAO.deallocateRoom(u.getId());
            }
            refreshStudentTable(table);
            clearForm(idField, nameField, deptField, contactField, roomField, passwordField);
        });

        deleteBtn.setOnAction(e -> {
            if (idField.getText().isEmpty() || !idField.isDisabled()) {
                showAlert("Error", "Select a student to delete.");
                return;
            }
            String id = idField.getText();
            roomDAO.deallocateRoom(id);
            userDAO.deleteUser(id);
            refreshStudentTable(table);
            clearForm(idField, nameField, deptField, contactField, roomField, passwordField);
        });

        clearBtn.setOnAction(e -> clearForm(idField, nameField, deptField, contactField, roomField, passwordField));

        vbox.getChildren().addAll(title, table, form, buttons);
        return vbox;
    }

    private void clearForm(TextField i, TextField n, TextField d, TextField c, TextField r, TextField p) {
        i.clear();
        n.clear();
        d.clear();
        c.clear();
        r.clear();
        p.clear();
        i.setDisable(false);
    }

    private void refreshStudentTable(TableView<User> table) {
        List<User> list = userDAO.getAllStudentsWithRooms();
        table.setItems(FXCollections.observableArrayList(list));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private VBox createComplaintView(User user) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Complaint Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<Complaint> table = new TableView<>();
        TableColumn<Complaint, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Complaint, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        TableColumn<Complaint, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<Complaint, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateLogged"));

        TableColumn<Complaint, String> remarksCol = new TableColumn<>("Remarks");
        remarksCol.setCellValueFactory(new PropertyValueFactory<>("remarks"));

        table.getColumns().addAll(idCol, descCol, statusCol, dateCol, remarksCol);

        // Load Data
        refreshComplaints(table, user);

        vbox.getChildren().addAll(title, table);

        // Add Complaint Form for Students
        if ("Student".equalsIgnoreCase(user.getRole())) {
            HBox form = new HBox(10);
            form.setAlignment(Pos.CENTER_LEFT);
            TextField descField = new TextField();
            descField.setPromptText("Enter complaint description...");
            descField.setPrefWidth(300);
            
            Button addBtn = new Button("Lodge Complaint");
            addBtn.setOnAction(e -> {
                if (descField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please enter a complaint description before lodging the complaint.");
                    return;
                }
                Complaint c = new Complaint();
                c.setStudentId(user.getId());
                c.setDescription(descField.getText().trim());
                c.setStatus("Pending");
                c.setDateLogged(LocalDate.now().toString());
                boolean added = complaintDAO.addComplaint(c);
                if (added) {
                    showAlert("Success", "Complaint lodged successfully.");
                    descField.clear();
                    refreshComplaints(table, user);
                } else {
                    showAlert("Error", "Unable to lodge complaint. Please try again.");
                }
            });
            
            form.getChildren().addAll(new Label("New Complaint:"), descField, addBtn);
            vbox.getChildren().add(form);
        } else {
            // Warden/Maintenance Update Form
            HBox updateForm = new HBox(10);
            updateForm.setAlignment(Pos.CENTER_LEFT);
            updateForm.setPadding(new Insets(10));
            updateForm.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px;");

            ComboBox<String> statusBox = new ComboBox<>();
            statusBox.getItems().addAll("Pending", "In-Progress", "Resolved");
            
            TextField remarksField = new TextField();
            remarksField.setPromptText("Add remarks...");
            
            Button updateBtn = new Button("Update Status");
            updateBtn.setDisable(true);

            Label selectedIdLabel = new Label();

            table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    selectedIdLabel.setText(String.valueOf(newVal.getId()));
                    statusBox.setValue(newVal.getStatus());
                    remarksField.setText(newVal.getRemarks());
                    updateBtn.setDisable(false);
                } else {
                    updateBtn.setDisable(true);
                }
            });

            updateBtn.setOnAction(e -> {
                if (!selectedIdLabel.getText().isEmpty()) {
                    Complaint c = new Complaint();
                    c.setId(Integer.parseInt(selectedIdLabel.getText()));
                    c.setStatus(statusBox.getValue());
                    c.setRemarks(remarksField.getText());
                    complaintDAO.updateComplaint(c);
                    refreshComplaints(table, user);
                    remarksField.clear();
                    statusBox.getSelectionModel().clearSelection();
                }
            });

            updateForm.getChildren().addAll(new Label("Update Complaint:"), statusBox, remarksField, updateBtn);
            vbox.getChildren().add(updateForm);
        }

        return vbox;
    }

    private void refreshComplaints(TableView<Complaint> table, User user) {
        List<Complaint> list;
        if ("Student".equalsIgnoreCase(user.getRole())) {
            list = complaintDAO.getComplaintsByStudent(user.getId());
        } else {
            // Warden/Maintenance see all
            list = complaintDAO.getAllComplaints();
        }
        table.setItems(FXCollections.observableArrayList(list));
    }

    private VBox createRoomManagementView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        Label title = new Label("Room Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<Room> table = new TableView<>();
        TableColumn<Room, String> roomCol = new TableColumn<>("Room No");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        
        TableColumn<Room, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        TableColumn<Room, Integer> capCol = new TableColumn<>("Capacity");
        capCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        
        TableColumn<Room, Integer> occCol = new TableColumn<>("Occupied");
        occCol.setCellValueFactory(new PropertyValueFactory<>("currentOccupancy"));

        table.getColumns().addAll(roomCol, typeCol, capCol, occCol);
        
        refreshRoomTable(table);

        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        TextField roomNoField = new TextField();
        TextField typeField = new TextField();
        TextField capField = new TextField();
        TextField occField = new TextField();

        form.add(new Label("Room No:"), 0, 0);
        form.add(roomNoField, 1, 0);
        form.add(new Label("Type:"), 2, 0);
        form.add(typeField, 3, 0);
        form.add(new Label("Capacity:"), 0, 1);
        form.add(capField, 1, 1);
        form.add(new Label("Occupied:"), 2, 1);
        form.add(occField, 3, 1);

        HBox buttons = new HBox(10);
        Button addBtn = new Button("Add Room");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button clearBtn = new Button("Clear");
        
        buttons.getChildren().addAll(addBtn, updateBtn, deleteBtn, clearBtn);

        // Selection
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                roomNoField.setText(newVal.getRoomNumber());
                typeField.setText(newVal.getType());
                capField.setText(String.valueOf(newVal.getCapacity()));
                occField.setText(String.valueOf(newVal.getCurrentOccupancy()));
                roomNoField.setDisable(true);
            }
        });

        // Actions
        addBtn.setOnAction(e -> {
            try {
                Room r = new Room();
                r.setRoomNumber(roomNoField.getText());
                r.setType(typeField.getText());
                r.setCapacity(Integer.parseInt(capField.getText()));
                r.setCurrentOccupancy(Integer.parseInt(occField.getText()));
                roomDAO.addRoom(r);
                refreshRoomTable(table);
                clearRoomForm(roomNoField, typeField, capField, occField);
            } catch (Exception ex) {
                showAlert("Error", "Invalid Input: " + ex.getMessage());
            }
        });

        updateBtn.setOnAction(e -> {
            Room selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            try {
                selected.setType(typeField.getText());
                selected.setCapacity(Integer.parseInt(capField.getText()));
                selected.setCurrentOccupancy(Integer.parseInt(occField.getText()));
                selected.setRoomNumber(roomNoField.getText()); // Ensure room number is set if needed for update logic
                roomDAO.updateRoom(selected);
                refreshRoomTable(table);
                clearRoomForm(roomNoField, typeField, capField, occField);
            } catch (Exception ex) {
                showAlert("Error", "Invalid Input: " + ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            Room selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            roomDAO.deleteRoom(selected.getId());
            refreshRoomTable(table);
            clearRoomForm(roomNoField, typeField, capField, occField);
        });

        clearBtn.setOnAction(e -> clearRoomForm(roomNoField, typeField, capField, occField));

        vbox.getChildren().addAll(title, table, form, buttons);
        return vbox;
    }

    private void refreshRoomTable(TableView<Room> table) {
        table.setItems(FXCollections.observableArrayList(roomDAO.getAllRooms()));
    }

    private void clearRoomForm(TextField r, TextField t, TextField c, TextField o) {
        r.clear();
        t.clear();
        c.clear();
        o.clear();
        r.setDisable(false);
    }

    private VBox createMessBillingView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Mess Billing Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<MessBill> table = new TableView<>();
        TableColumn<MessBill, String> idCol = new TableColumn<>("Bill ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<MessBill, String> studentCol = new TableColumn<>("Student ID");
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        
        TableColumn<MessBill, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        TableColumn<MessBill, String> monthCol = new TableColumn<>("Month");
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        
        TableColumn<MessBill, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        
        TableColumn<MessBill, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, studentCol, amountCol, monthCol, yearCol, statusCol);
        
        table.setItems(FXCollections.observableArrayList(messBillDAO.getAllBills()));

        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        ComboBox<String> studentBox = new ComboBox<>();
        studentBox.setEditable(true); // Allow manual entry
        List<User> students = userDAO.getAllStudentsWithRooms();
        for (User s : students) {
            studentBox.getItems().add(s.getId());
        }

        TextField amountField = new TextField();
        ComboBox<String> monthBox = new ComboBox<>();
        monthBox.getItems().addAll("January", "February", "March", "April", "May", "June", 
                                   "July", "August", "September", "October", "November", "December");
        
        TextField yearField = new TextField();
        yearField.setText(String.valueOf(LocalDate.now().getYear()));

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Paid", "Unpaid");
        statusBox.setValue("Unpaid");

        form.add(new Label("Student ID:"), 0, 0);
        form.add(studentBox, 1, 0);
        form.add(new Label("Amount:"), 2, 0);
        form.add(amountField, 3, 0);
        form.add(new Label("Month:"), 0, 1);
        form.add(monthBox, 1, 1);
        form.add(new Label("Year:"), 2, 1);
        form.add(yearField, 3, 1);
        form.add(new Label("Status:"), 0, 2);
        form.add(statusBox, 1, 2);

        HBox buttons = new HBox(10);
        Button generateBtn = new Button("Generate Bill");
        Button updateBtn = new Button("Update Bill");
        Button clearBtn = new Button("Clear");
        
        buttons.getChildren().addAll(generateBtn, updateBtn, clearBtn);

        // Selection Listener
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                studentBox.setValue(newVal.getStudentId());
                amountField.setText(String.valueOf(newVal.getAmount()));
                monthBox.setValue(newVal.getMonth());
                yearField.setText(String.valueOf(newVal.getYear()));
                statusBox.setValue(newVal.getStatus());
            }
        });
        
        generateBtn.setOnAction(e -> {
            try {
                if (studentBox.getValue() == null || amountField.getText().isEmpty() || monthBox.getValue() == null) {
                    showAlert("Error", "All fields are required.");
                    return;
                }
                
                MessBill bill = new MessBill();
                bill.setStudentId(studentBox.getValue());
                bill.setAmount(Double.parseDouble(amountField.getText()));
                bill.setMonth(monthBox.getValue());
                bill.setYear(Integer.parseInt(yearField.getText()));
                bill.setStatus(statusBox.getValue() != null ? statusBox.getValue() : "Unpaid");
                bill.setGeneratedDate(LocalDate.now().toString());
                
                messBillDAO.addBill(bill);
                table.setItems(FXCollections.observableArrayList(messBillDAO.getAllBills()));
                
                clearMessForm(studentBox, amountField, monthBox, yearField, statusBox);
            } catch (Exception ex) {
                showAlert("Error", "Invalid Input: " + ex.getMessage());
            }
        });

        updateBtn.setOnAction(e -> {
            MessBill selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Select a bill to update.");
                return;
            }
            try {
                selected.setStudentId(studentBox.getValue());
                selected.setAmount(Double.parseDouble(amountField.getText()));
                selected.setMonth(monthBox.getValue());
                selected.setYear(Integer.parseInt(yearField.getText()));
                selected.setStatus(statusBox.getValue());
                
                messBillDAO.updateBill(selected);
                table.setItems(FXCollections.observableArrayList(messBillDAO.getAllBills()));
                clearMessForm(studentBox, amountField, monthBox, yearField, statusBox);
            } catch (Exception ex) {
                showAlert("Error", "Invalid Input: " + ex.getMessage());
            }
        });

        clearBtn.setOnAction(e -> clearMessForm(studentBox, amountField, monthBox, yearField, statusBox));

        vbox.getChildren().addAll(title, table, form, buttons);
        return vbox;
    }

    private void clearMessForm(ComboBox<String> s, TextField a, ComboBox<String> m, TextField y, ComboBox<String> st) {
        s.setValue(null);
        a.clear();
        m.setValue(null);
        y.setText(String.valueOf(LocalDate.now().getYear()));
        st.setValue("Unpaid");
    }

    private VBox createStudentMessBillView(User user) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label title = new Label("My Mess Bills");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<MessBill> table = new TableView<>();
        
        TableColumn<MessBill, String> monthCol = new TableColumn<>("Month");
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        
        TableColumn<MessBill, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        
        TableColumn<MessBill, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        TableColumn<MessBill, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<MessBill, String> dateCol = new TableColumn<>("Date Generated");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("generatedDate"));

        table.getColumns().addAll(monthCol, yearCol, amountCol, statusCol, dateCol);
        
        table.setItems(FXCollections.observableArrayList(messBillDAO.getBillsByStudent(user.getId())));

        vbox.getChildren().addAll(title, table);
        return vbox;
    }

    private VBox createStudentPermissionView(User user) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Apply for Permission");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Table for history
        TableView<Permission> table = new TableView<>();
        TableColumn<Permission, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        TableColumn<Permission, String> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        
        TableColumn<Permission, String> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        
        TableColumn<Permission, String> startTimeCol = new TableColumn<>("Start Time");
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        
        TableColumn<Permission, String> endTimeCol = new TableColumn<>("End Time");
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        
        TableColumn<Permission, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        
        TableColumn<Permission, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(typeCol, startCol, endCol, startTimeCol, endTimeCol, reasonCol, statusCol);
        table.setItems(FXCollections.observableArrayList(permissionDAO.getPermissionsByStudent(user.getId())));

        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Leave", "Outing");
        
        DatePicker startDate = new DatePicker();
        DatePicker endDate = new DatePicker();
        
        TextField startTimeField = new TextField();
        startTimeField.setPromptText("HH:mm");
        ComboBox<String> startAmPm = new ComboBox<>();
        startAmPm.getItems().addAll("AM", "PM");
        startAmPm.setValue("AM");
        HBox startTimeBox = new HBox(5, startTimeField, startAmPm);

        TextField endTimeField = new TextField();
        endTimeField.setPromptText("HH:mm");
        ComboBox<String> endAmPm = new ComboBox<>();
        endAmPm.getItems().addAll("AM", "PM");
        endAmPm.setValue("PM");
        HBox endTimeBox = new HBox(5, endTimeField, endAmPm);

        TextField reasonField = new TextField();

        form.add(new Label("Type:"), 0, 0);
        form.add(typeBox, 1, 0);
        form.add(new Label("Start Date:"), 2, 0);
        form.add(startDate, 3, 0);
        form.add(new Label("End Date:"), 0, 1);
        form.add(endDate, 1, 1);
        form.add(new Label("Start Time:"), 2, 1);
        form.add(startTimeBox, 3, 1);
        form.add(new Label("End Time:"), 0, 2);
        form.add(endTimeBox, 1, 2);
        form.add(new Label("Reason:"), 2, 2);
        form.add(reasonField, 3, 2);

        Button applyBtn = new Button("Apply");
        applyBtn.setOnAction(e -> {
            try {
                if (typeBox.getValue() == null || startDate.getValue() == null || endDate.getValue() == null || 
                    startTimeField.getText().isEmpty() || endTimeField.getText().isEmpty() || reasonField.getText().isEmpty()) {
                    showAlert("Error", "All fields are required.");
                    return;
                }
                
                Permission p = new Permission();
                p.setStudentId(user.getId());
                p.setType(typeBox.getValue());
                p.setStartDate(startDate.getValue().toString());
                p.setEndDate(endDate.getValue().toString());
                p.setStartTime(startTimeField.getText() + " " + startAmPm.getValue());
                p.setEndTime(endTimeField.getText() + " " + endAmPm.getValue());
                p.setReason(reasonField.getText());
                p.setStatus("Pending");
                
                permissionDAO.addPermission(p);
                table.setItems(FXCollections.observableArrayList(permissionDAO.getPermissionsByStudent(user.getId())));
                
                typeBox.setValue(null);
                startDate.setValue(null);
                endDate.setValue(null);
                startTimeField.clear();
                endTimeField.clear();
                startAmPm.setValue("AM");
                endAmPm.setValue("PM");
                reasonField.clear();
            } catch (Exception ex) {
                showAlert("Error", "Invalid Input: " + ex.getMessage());
            }
        });

        vbox.getChildren().addAll(title, table, form, applyBtn);
        return vbox;
    }

    private VBox createWardenPermissionView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Manage Permissions");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<Permission> table = new TableView<>();
        TableColumn<Permission, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Permission, String> studentCol = new TableColumn<>("Student ID");
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<Permission, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        TableColumn<Permission, String> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        
        TableColumn<Permission, String> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        
        TableColumn<Permission, String> startTimeCol = new TableColumn<>("Start Time");
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        
        TableColumn<Permission, String> endTimeCol = new TableColumn<>("End Time");
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        
        TableColumn<Permission, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        
        TableColumn<Permission, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, studentCol, typeCol, startCol, endCol, startTimeCol, endTimeCol, reasonCol, statusCol);
        table.setItems(FXCollections.observableArrayList(permissionDAO.getAllPermissions()));

        HBox buttons = new HBox(10);
        Button approveBtn = new Button("Approve");
        Button rejectBtn = new Button("Reject");
        
        buttons.getChildren().addAll(approveBtn, rejectBtn);

        approveBtn.setOnAction(e -> {
            Permission selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                permissionDAO.updatePermissionStatus(selected.getId(), "Approved");
                table.setItems(FXCollections.observableArrayList(permissionDAO.getAllPermissions()));
            } else {
                showAlert("Error", "Select a request to approve.");
            }
        });

        rejectBtn.setOnAction(e -> {
            Permission selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                permissionDAO.updatePermissionStatus(selected.getId(), "Rejected");
                table.setItems(FXCollections.observableArrayList(permissionDAO.getAllPermissions()));
            } else {
                showAlert("Error", "Select a request to reject.");
            }
        });

        vbox.getChildren().addAll(title, table, buttons);
        return vbox;
    }

    private VBox createAttendanceView() {
        VBox mainVBox = new VBox(10);
        mainVBox.setPadding(new Insets(20));

        Label title = new Label("Attendance Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TabPane tabPane = new TabPane();

        // Tab 1: Mark Attendance
        Tab markTab = new Tab("Mark Attendance");
        markTab.setClosable(false);
        markTab.setContent(createMarkAttendanceContent());

        // Tab 2: View Attendance Report
        Tab viewTab = new Tab("View Attendance Report");
        viewTab.setClosable(false);
        viewTab.setContent(createViewAttendanceContent());

        tabPane.getTabs().addAll(markTab, viewTab);

        mainVBox.getChildren().addAll(title, tabPane);
        return mainVBox;
    }

    private VBox createMarkAttendanceContent() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        HBox dateBox = new HBox(10);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        DatePicker datePicker = new DatePicker(LocalDate.now());
        Button loadBtn = new Button("Load Stats");
        dateBox.getChildren().addAll(new Label("Date:"), datePicker, loadBtn);

        TableView<User> table = new TableView<>();
        TableColumn<User, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty("Select below")); 

        table.getColumns().addAll(idCol, nameCol, statusCol);
        table.setItems(FXCollections.observableArrayList(userDAO.getAllStudentsWithRooms()));

        // Attendance Marking Area
        VBox markingArea = new VBox(10);
        markingArea.setPadding(new Insets(10));
        markingArea.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-padding: 10px;");
        
        Label selectedStudentLabel = new Label("Select a student to mark attendance");
        selectedStudentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        HBox radioBox = new HBox(20);
        RadioButton presentRadio = new RadioButton("Present");
        RadioButton absentRadio = new RadioButton("Absent");
        ToggleGroup group = new ToggleGroup();
        presentRadio.setToggleGroup(group);
        absentRadio.setToggleGroup(group);
        radioBox.getChildren().addAll(presentRadio, absentRadio);
        
        Button markBtn = new Button("Mark Attendance");
        
        markingArea.getChildren().addAll(selectedStudentLabel, radioBox, markBtn);

        // Summary Area
        HBox summaryBox = new HBox(20);
        Label presentCountLabel = new Label("Present: 0");
        Label absentCountLabel = new Label("Absent: 0");
        presentCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        presentCountLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        absentCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        absentCountLabel.setTextFill(javafx.scene.paint.Color.RED);
        summaryBox.getChildren().addAll(presentCountLabel, absentCountLabel);

        // Logic
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedStudentLabel.setText("Marking for: " + newVal.getFullName() + " (" + newVal.getId() + ")");
                group.selectToggle(null); // Reset selection
            }
        });

        markBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null || datePicker.getValue() == null || group.getSelectedToggle() == null) {
                showAlert("Error", "Select student, date and status.");
                return;
            }
            
            String status = ((RadioButton) group.getSelectedToggle()).getText();
            Attendance a = new Attendance();
            a.setStudentId(selected.getId());
            a.setDate(datePicker.getValue().toString());
            a.setStatus(status);
            
            attendanceDAO.markAttendance(a);
            showAlert("Success", "Attendance marked for " + selected.getFullName());
            
            // Update summary
            int present = attendanceDAO.getCountByStatus(datePicker.getValue().toString(), "Present");
            int absent = attendanceDAO.getCountByStatus(datePicker.getValue().toString(), "Absent");
            presentCountLabel.setText("Present: " + present);
            absentCountLabel.setText("Absent: " + absent);
        });
        
        loadBtn.setOnAction(e -> {
            if (datePicker.getValue() != null) {
                int present = attendanceDAO.getCountByStatus(datePicker.getValue().toString(), "Present");
                int absent = attendanceDAO.getCountByStatus(datePicker.getValue().toString(), "Absent");
                presentCountLabel.setText("Present: " + present);
                absentCountLabel.setText("Absent: " + absent);
            }
        });

        vbox.getChildren().addAll(dateBox, summaryBox, table, markingArea);
        return vbox;
    }

    private VBox createViewAttendanceContent() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        HBox dateBox = new HBox(10);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        DatePicker datePicker = new DatePicker(LocalDate.now());
        Button loadBtn = new Button("Load Report");
        dateBox.getChildren().addAll(new Label("Date:"), datePicker, loadBtn);

        TableView<AttendanceDetail> table = new TableView<>();
        TableColumn<AttendanceDetail, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        
        TableColumn<AttendanceDetail, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        
        TableColumn<AttendanceDetail, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        
        TableColumn<AttendanceDetail, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        table.getColumns().addAll(idCol, nameCol, roomCol, statusCol);
        
        Label summaryLabel = new Label("Summary: -");
        summaryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        loadBtn.setOnAction(e -> {
            if (datePicker.getValue() != null) {
                String date = datePicker.getValue().toString();
                List<AttendanceDetail> list = attendanceDAO.getAttendanceDetailsByDate(date);
                table.setItems(FXCollections.observableArrayList(list));
                
                long present = list.stream().filter(a -> "Present".equals(a.getStatus())).count();
                long absent = list.stream().filter(a -> "Absent".equals(a.getStatus())).count();
                summaryLabel.setText("Summary: Present: " + present + ", Absent: " + absent);
            }
        });

        vbox.getChildren().addAll(dateBox, summaryLabel, table);
        return vbox;
    }

    private javafx.scene.control.ScrollPane createHomeView(User user) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_CENTER);
        // Subtle gradient background for home view
        content.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f2f5, #e2e6ea);");

        // Header Section
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        Label welcomeLabel = new Label("Welcome back, " + user.getFullName() + "!");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(javafx.scene.paint.Color.web("#2c3e50"));
        welcomeLabel.setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.3), 2, 0, 0, 0);");
        
        Label dateLabel = new Label(java.time.format.DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy").format(LocalDate.now()));
        dateLabel.setFont(Font.font("Segoe UI", 16));
        dateLabel.setTextFill(javafx.scene.paint.Color.web("#7f8c8d"));
        
        headerBox.getChildren().addAll(welcomeLabel, dateLabel);
        content.getChildren().add(headerBox);

        // Role Specific Content
        if ("Warden".equalsIgnoreCase(user.getRole()) || "Admin".equalsIgnoreCase(user.getRole())) {
            content.getChildren().add(createWardenDashboardStats());
        } else if ("Staff".equalsIgnoreCase(user.getRole())) {
            content.getChildren().add(createStaffDashboardStats());
        } else if ("Student".equalsIgnoreCase(user.getRole())) {
            content.getChildren().add(createStudentDashboardStats(user));
        }

        // Message Section
        content.getChildren().add(createMessageSection(user));

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;"); // Ensure transparency
        return scrollPane;
    }

    private VBox createWardenDashboardStats() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Stats Calculation
        int totalStudents = userDAO.getAllStudentsWithRooms().size();
        List<Room> rooms = roomDAO.getAllRooms();
        int totalCapacity = rooms.stream().mapToInt(Room::getCapacity).sum();
        int currentOccupancy = rooms.stream().mapToInt(Room::getCurrentOccupancy).sum();
        long pendingComplaints = complaintDAO.getAllComplaints().stream().filter(c -> "Pending".equalsIgnoreCase(c.getStatus())).count();
        int presentToday = attendanceDAO.getCountByStatus(LocalDate.now().toString(), "Present");

        // Cards with enhanced colors
        grid.add(createStatCard("Total Students", String.valueOf(totalStudents), "linear-gradient(to right, #3498db, #2980b9)"), 0, 0);
        grid.add(createStatCard("Room Occupancy", currentOccupancy + "/" + totalCapacity, "linear-gradient(to right, #e67e22, #d35400)"), 1, 0);
        grid.add(createStatCard("Pending Complaints", String.valueOf(pendingComplaints), "linear-gradient(to right, #e74c3c, #c0392b)"), 0, 1);
        grid.add(createStatCard("Present Today", String.valueOf(presentToday), "linear-gradient(to right, #2ecc71, #27ae60)"), 1, 1);

        VBox container = new VBox(20, grid);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private VBox createStaffDashboardStats() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        int presentToday = attendanceDAO.getCountByStatus(LocalDate.now().toString(), "Present");
        int absentToday = attendanceDAO.getCountByStatus(LocalDate.now().toString(), "Absent");
        
        grid.add(createStatCard("Present Today", String.valueOf(presentToday), "linear-gradient(to right, #2ecc71, #27ae60)"), 0, 0);
        grid.add(createStatCard("Absent Today", String.valueOf(absentToday), "linear-gradient(to right, #e74c3c, #c0392b)"), 1, 0);

        VBox container = new VBox(20, grid);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private VBox createStudentDashboardStats(User user) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Find Room
        String roomNo = "Not Allocated";
        for(User u : userDAO.getAllStudentsWithRooms()) {
            if(u.getId() != null && user.getId() != null && u.getId().equals(user.getId()) && u.getRoomNumber() != null) {
                roomNo = u.getRoomNumber();
                break;
            }
        }

        long myPendingComplaints = complaintDAO.getComplaintsByStudent(user.getId()).stream().filter(c -> "Pending".equalsIgnoreCase(c.getStatus())).count();
        
        grid.add(createStatCard("My Room", roomNo, "linear-gradient(to right, #9b59b6, #8e44ad)"), 0, 0);
        grid.add(createStatCard("Pending Complaints", String.valueOf(myPendingComplaints), "linear-gradient(to right, #f39c12, #d35400)"), 1, 0);

        VBox container = new VBox(20, grid);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private VBox createStatCard(String title, String value, String bgGradient) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefSize(220, 130);
        card.setAlignment(Pos.CENTER);
        // Enhanced card style with gradient background and white text
        card.setStyle("-fx-background-color: " + bgGradient + "; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        valueLabel.setTextFill(javafx.scene.paint.Color.WHITE);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", 14));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#ecf0f1"));

        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }

    private VBox createWardenMessageView(User currentUser) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Message Center");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        HBox mainContent = new HBox(20);
        
        // User List
        VBox userListVBox = new VBox(10);
        userListVBox.setPrefWidth(250);
        userListVBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10;");
        Label selectUserLabel = new Label("Select User:");
        
        TableView<User> userTable = new TableView<>();
        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<User, String> usernameCol = new TableColumn<>("Login");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        userTable.getColumns().addAll(nameCol, usernameCol, roleCol);
        
        // Load only students so the warden can select a particular student login
        List<User> allStudents = userDAO.getAllUsers().stream()
                .filter(u -> u.getId() != null && !u.getId().equals(currentUser.getId()) && "Student".equalsIgnoreCase(u.getRole()))
                .collect(java.util.stream.Collectors.toList());
        userTable.setItems(FXCollections.observableArrayList(allStudents));

        userListVBox.getChildren().addAll(selectUserLabel, userTable);

        // Chat Area
        VBox chatArea = new VBox(10);
        chatArea.setPrefWidth(500);
        chatArea.setStyle("-fx-border-color: #ccc; -fx-padding: 10;");
        
        Label chatWithLabel = new Label("Chat");
        chatWithLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        javafx.scene.control.TextArea conversationArea = new javafx.scene.control.TextArea();
        conversationArea.setEditable(false);
        conversationArea.setPrefHeight(300);
        
        HBox inputBox = new HBox(10);
        TextField messageField = new TextField();
        messageField.setPromptText("Enter message for selected student login");
        messageField.setPrefWidth(350);
        Button sendBtn = new Button("Send");
        inputBox.getChildren().addAll(messageField, sendBtn);
        
        chatArea.getChildren().addAll(chatWithLabel, conversationArea, inputBox);

        // Logic
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                chatWithLabel.setText("Chat with: " + newVal.getFullName() + " (" + newVal.getUsername() + ")");
                loadConversation(currentUser.getId(), newVal.getId(), conversationArea);
            }
        });

        sendBtn.setOnAction(e -> {
            User selected = userTable.getSelectionModel().getSelectedItem();
            String content = messageField.getText();
            if (selected == null) {
                showAlert("Error", "Please select a student login before sending a message.");
                return;
            }
            if (content == null || content.trim().isEmpty()) {
                showAlert("Error", "Please enter a message before sending.");
                return;
            }

            Message m = new Message();
            m.setSenderId(currentUser.getId());
            m.setReceiverId(selected.getId());
            m.setContent(content.trim());
            m.setTimestamp(java.time.LocalDateTime.now().toString());

            messageDAO.sendMessage(m);
            messageField.clear();
            loadConversation(currentUser.getId(), selected.getId(), conversationArea);
            showAlert("Success", "Message sent to " + selected.getFullName() + " (" + selected.getUsername() + ").");
        });

        mainContent.getChildren().addAll(userListVBox, chatArea);
        vbox.getChildren().addAll(title, mainContent);
        return vbox;
    }

    private void loadConversation(String user1, String user2, javafx.scene.control.TextArea area) {
        List<Message> msgs = messageDAO.getConversation(user1, user2);
        StringBuilder sb = new StringBuilder();
        for (Message m : msgs) {
            sb.append(m.getSenderName()).append(" [").append(m.getTimestamp()).append("]:\n");
            sb.append(m.getContent()).append("\n\n");
        }
        area.setText(sb.toString());
        area.setScrollTop(Double.MAX_VALUE);
    }

    private VBox createStudentMessageView(User user) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Messages from Warden");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        javafx.scene.control.TextArea conversationArea = new javafx.scene.control.TextArea();
        conversationArea.setEditable(false);
        conversationArea.setPrefHeight(400);
        conversationArea.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");

        // Load conversation with warden (ID: W001)
        loadConversation(user.getId(), "W001", conversationArea);

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadConversation(user.getId(), "W001", conversationArea));

        HBox buttonBox = new HBox(refreshBtn);
        buttonBox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(title, conversationArea, buttonBox);
        return vbox;
    }

    private VBox createMessageSection(User user) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        
        Label title = new Label("Recent Messages / Announcements");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        title.setTextFill(javafx.scene.paint.Color.web("#2c3e50"));

        javafx.scene.control.ListView<String> msgList = new javafx.scene.control.ListView<>();
        msgList.setPrefHeight(150);
        
        List<Message> msgs = messageDAO.getMessagesForUser(user.getId());
        for (Message m : msgs) {
            String prefix = m.getSenderId().equals(user.getId()) ? "You: " : m.getSenderName() + ": ";
            msgList.getItems().add(prefix + m.getContent() + " (" + m.getTimestamp() + ")");
        }
        
        // Reply Feature for Home Page
        HBox replyBox = new HBox(10);
        TextField replyField = new TextField();
        replyField.setPromptText("Reply to last sender...");
        replyField.setPrefWidth(300);
        Button replyBtn = new Button("Reply");
        
        replyBtn.setOnAction(e -> {
            if (!msgs.isEmpty() && !replyField.getText().trim().isEmpty()) {
                // Reply to the sender of the last received message (if any)
                // Find last message where receiver is current user
                Message lastReceived = msgs.stream()
                    .filter(m -> m.getReceiverId().equals(user.getId()))
                    .findFirst() // Since list is ordered DESC
                    .orElse(null);
                
                if (lastReceived != null) {
                    Message reply = new Message();
                    reply.setSenderId(user.getId());
                    reply.setReceiverId(lastReceived.getSenderId());
                    reply.setContent(replyField.getText());
                    reply.setTimestamp(java.time.LocalDateTime.now().toString());
                    messageDAO.sendMessage(reply);
                    replyField.clear();
                    showAlert("Success", "Reply sent.");
                    // Refresh list
                    msgList.getItems().clear();
                    List<Message> newMsgs = messageDAO.getMessagesForUser(user.getId());
                    for (Message m : newMsgs) {
                        String prefix = m.getSenderId().equals(user.getId()) ? "You: " : m.getSenderName() + ": ";
                        msgList.getItems().add(prefix + m.getContent() + " (" + m.getTimestamp() + ")");
                    }
                } else {
                    showAlert("Info", "No message to reply to.");
                }
            }
        });
        
        replyBox.getChildren().addAll(replyField, replyBtn);
        vbox.getChildren().addAll(title, msgList, replyBox);
        return vbox;
    }
}
