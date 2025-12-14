package ru.Dmitriy.NauJava_ToDo_List.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Dmitriy.NauJava_ToDo_List.entity.User;
import ru.Dmitriy.NauJava_ToDo_List.service.UserService;
import java.io.IOException;

@Component
public class AuthController {

    private static User currentUser;

    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Button loginButton;
    @FXML private Hyperlink registerLink;
    @FXML private Label loginError;

    @FXML private TextField registerFirstName;
    @FXML private TextField registerLastName;
    @FXML private TextField registerEmail;
    @FXML private TextField registerLogin;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField registerConfirmPassword;
    @FXML private Button registerButton;
    @FXML private Hyperlink loginLink;
    @FXML private Label registerError;

    @Autowired
    private UserService userService;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> handleRegister());

        loginLink.setOnAction(e -> switchToLogin());
        registerLink.setOnAction(e -> switchToRegister());

        loginUsername.setOnAction(e -> handleLogin());
        loginPassword.setOnAction(e -> handleLogin());

        registerPassword.setOnAction(e -> handleRegister());
        registerConfirmPassword.setOnAction(e -> handleRegister());
    }

    private void handleLogin() {
        String username = loginUsername.getText();
        String password = loginPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            loginError.setText("Заполните все поля");
            return;
        }

        try {
            User user = userService.authenticate(username, password);
            currentUser = user;
            switchToMainView();
            clearLoginFields();
        } catch (Exception e) {
            loginError.setText("Ошибка входа: " + e.getMessage());
        }
    }

    private void handleRegister() {
        String firstName = registerFirstName.getText().trim();
        String lastName = registerLastName.getText().trim();
        String email = registerEmail.getText().trim();
        String login = registerLogin.getText().trim();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();

        StringBuilder errorMessage = new StringBuilder();

        if (firstName.isEmpty()) errorMessage.append("Имя обязательно\n");
        if (lastName.isEmpty()) errorMessage.append("Фамилия обязательна\n");
        if (email.isEmpty()) errorMessage.append("Email обязателен\n");
        if (login.isEmpty()) errorMessage.append("Логин обязателен\n");
        if (password.isEmpty()) errorMessage.append("Пароль обязателен\n");

        if (!email.isEmpty() && !isValidEmail(email)) {
            errorMessage.append("Некорректный формат email\n");
        }

        if (!password.isEmpty() && password.length() < 8) {
            errorMessage.append("Пароль должен содержать минимум 8 символов\n");
        }

        if (!password.equals(confirmPassword)) {
            errorMessage.append("Пароли не совпадают\n");
        }


        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                login.isEmpty() || password.isEmpty()) {
            registerError.setText("Заполните все обязательные поля");
            return;
        }


        if (errorMessage.length() > 0) {
            registerError.setText(errorMessage.toString());
            return;
        }

        try {
            if (userService.existsByLogin(login)) {
                registerError.setText("Пользователь с таким логином уже существует");
                return;
            }

            if (userService.existsByEmail(email)) {
                registerError.setText("Пользователь с таким email уже существует");
                return;
            }

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setLogin(login);
            user.setPassword(password);

            User createdUser = userService.createUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Успех",
                        "Пользователь " + createdUser.getFullName() + " успешно создан!");
            switchToLogin();

            loginUsername.setText(login);
            loginPassword.clear();

        }catch (Exception e) {
            registerError.setText("Ошибка регистрации: " + e.getMessage());
        }
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private void clearLoginFields() {
        loginUsername.clear();
        loginPassword.clear();
        loginError.setText("");
    }

    private void clearRegisterFields() {
        registerFirstName.clear();
        registerLastName.clear();
        registerEmail.clear();
        registerLogin.clear();
        registerPassword.clear();
        registerConfirmPassword.clear();
        registerError.setText("");
    }

    private void switchToLogin() {
        try {
            clearRegisterFields();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/auth/LoginView.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToRegister() {
        try {
            clearLoginFields();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/auth/RegisterView.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main/MainView.fxml"));
            Parent root = loader.load();
            MainController mainController = loader.getController();
            mainController.setCurrentUser(currentUser);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean existsByLogin(String login) {
        return userService.existsByLogin(login);
    }

    private boolean existsByEmail(String email) {
        return userService.existsByEmail(email);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.showAndWait();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }

}
