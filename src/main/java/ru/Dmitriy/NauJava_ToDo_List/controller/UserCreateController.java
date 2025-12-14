package ru.Dmitriy.NauJava_ToDo_List.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Dmitriy.NauJava_ToDo_List.entity.User;
import ru.Dmitriy.NauJava_ToDo_List.service.UserService;

@Component
public class UserCreateController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private CheckBox activeCheckBox;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label errorLabel;

    @Autowired
    private UserService userService;

    private Stage dialogStage;
    private MainController parentController;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());

        // Установка значения по умолчанию
        activeCheckBox.setSelected(true);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setParentController(MainController parentController) {
        this.parentController = parentController;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            User user = new User();
            user.setFirstName(firstNameField.getText().trim());
            user.setLastName(lastNameField.getText().trim());
            user.setEmail(emailField.getText().trim());
            user.setLogin(loginField.getText().trim());
            user.setPassword(passwordField.getText());
            user.setActive(activeCheckBox.isSelected());

            try {
                userService.createUser(user);
                okClicked = true;
                dialogStage.close();
            } catch (Exception e) {
                errorLabel.setText("Ошибка: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        if (firstNameField.getText() == null || firstNameField.getText().trim().isEmpty()) {
            errorMessage.append("Имя обязательно\n");
        }

        if (lastNameField.getText() == null || lastNameField.getText().trim().isEmpty()) {
            errorMessage.append("Фамилия обязательна\n");
        }

        if (emailField.getText() == null || emailField.getText().trim().isEmpty()) {
            errorMessage.append("Email обязателен\n");
        } else if (!isValidEmail(emailField.getText().trim())) {
            errorMessage.append("Некорректный формат email\n");
        }

        if (loginField.getText() == null || loginField.getText().trim().isEmpty()) {
            errorMessage.append("Логин обязателен\n");
        }

        if (passwordField.getText() == null || passwordField.getText().isEmpty()) {
            errorMessage.append("Пароль обязателен\n");
        } else if (passwordField.getText().length() < 8) {
            errorMessage.append("Пароль должен содержать минимум 8 символов\n");
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errorMessage.append("Пароли не совпадают\n");
        }

        if (errorMessage.length() > 0) {
            errorLabel.setText(errorMessage.toString());
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}
