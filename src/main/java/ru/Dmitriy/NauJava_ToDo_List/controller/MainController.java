package ru.Dmitriy.NauJava_ToDo_List.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Dmitriy.NauJava_ToDo_List.entity.User;
import ru.Dmitriy.NauJava_ToDo_List.service.UserService;

import java.io.IOException;

@Component
public class MainController {

    @FXML private Label welcomeLabel;
    @FXML private Label userInfoLabel;
    @FXML private BorderPane mainBorderPane;
    @FXML private MenuBar mainMenuBar;
    @FXML private TabPane mainTabPane;

    @FXML private Tab usersTab;
    @FXML private Tab tasksTab;
    @FXML private Tab statisticsTab;

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Long> idColumn;
    @FXML private TableColumn<User, String> fullNameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> loginColumn;
    @FXML private TableColumn<User, Boolean> activeColumn;

    @FXML private Button addUserButton;
    @FXML private Button editUserButton;
    @FXML private Button deleteUserButton;
    @FXML private Button refreshUsersButton;

    @FXML private MenuItem logoutMenuItem;

    @Autowired
    private UserService userService;

    private User currentUser;
    private Stage stage;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserInfo();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        setupUsersTable();
        loadUsers();

        logoutMenuItem.setOnAction(e -> handleLogout());

        addUserButton.setOnAction(e -> showAddUserDialog());
        editUserButton.setOnAction(e -> showEditUserDialog());
        deleteUserButton.setOnAction(e -> deleteSelectedUser());
        refreshUsersButton.setOnAction(e -> loadUsers());
    }


    private void updateUserInfo() {
        if (currentUser != null) {
            welcomeLabel.setText("Добро пожаловать, " + currentUser.getFirstName() + "!");
            userInfoLabel.setText("Вы вошли как: " + currentUser.getFullName() +
                    " (" + currentUser.getEmail() + ")");
        }
    }

    private void setupUsersTable() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        fullNameColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        loginColumn.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        activeColumn.setCellValueFactory(cellData -> cellData.getValue().activeProperty());

        activeColumn.setCellFactory(column -> new TableCell<User, Boolean>() {
            @Override
            protected void updateItem(Boolean active, boolean empty) {
                super.updateItem(active, empty);
                if (empty || active == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(active ? "Активен" : "Неактивен");
                    setStyle(active ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                }
            }
        });
    }

    private void loadUsers() {
        usersTable.getItems().clear();
        usersTable.getItems().addAll(userService.getAllUsersSorted());
    }

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение выхода");
        confirm.setHeaderText("Выйти из системы?");
        confirm.setContentText("Вы уверены, что хотите выйти?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                performLogout();
            }
        });
    }

    private void performLogout() {
        try {
            Stage currentStage = (Stage) mainBorderPane.getScene().getWindow();

            AuthController.logout();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/auth/LoginView.fxml"));
            Parent root = loader.load();

            AuthController authController = loader.getController();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            currentStage.setScene(scene);
            currentStage.setMaximized(false);
            currentStage.setWidth(800);
            currentStage.setHeight(600);
            currentStage.centerOnScreen();
            currentStage.setTitle("NauJava Todo List - Вход");

            authController.setStage(currentStage);

            this.currentUser = null;

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка",
                    "Не удалось перейти на экран входа: " + e.getMessage());
        }
    }

    private void showAddUserDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/UserCreateView.fxml"));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Добавить пользователя");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(mainBorderPane.getScene().getWindow());

            Scene scene = new Scene(root);
            dialog.setScene(scene);

            UserCreateController controller = loader.getController();
            controller.setDialogStage(dialog);
            controller.setParentController(this);

            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditUserDialog() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите пользователя для редактирования");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/UserEditView.fxml"));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Редактировать пользователя");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(mainBorderPane.getScene().getWindow());

            Scene scene = new Scene(root);
            dialog.setScene(scene);

            UserEditController controller = loader.getController();
            controller.setDialogStage(dialog);
            controller.setParentController(this);

            dialog.showAndWait();

            if (controller.isOkClicked()) {
                loadUsers();
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка",
                    "Не удалось открыть форму редактирования пользователя: " + e.getMessage());
        }
    }

    private void deleteSelectedUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите пользователя для удаления");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение удаления");
        confirm.setHeaderText("Удалить пользователя?");
        confirm.setContentText("Вы уверены, что хотите удалить пользователя " +
                selectedUser.getFullName() + "?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                userService.deactivateUser(selectedUser.getId());
                loadUsers();
                showAlert(Alert.AlertType.INFORMATION, "Успех",
                        "Пользователь деактивирован");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Ошибка",
                        "Не удалось деактивировать пользователя: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("О программе");
        about.setHeaderText("NauJava Todo List");
        about.setContentText("Версия 1.0\nРазработано для курса Java");
        about.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
