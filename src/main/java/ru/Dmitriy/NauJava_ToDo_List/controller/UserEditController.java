package ru.Dmitriy.NauJava_ToDo_List.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Dmitriy.NauJava_ToDo_List.entity.User;
import ru.Dmitriy.NauJava_ToDo_List.service.UserService;

public class UserEditController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField loginField;
    @FXML private CheckBox activeCheckBox;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label errorLabel;
    @FXML private Label userIdLabel;

    @Autowired
    private UserService userService;

    private Stage dialogStage;
    private MainController parentController;
    private User user;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setParentController(MainController parentController) {
        this.parentController = parentController;
    }

    public void setUser(User user) {
        this.user = user;
        populateFields();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    private void populateFields() {
        if (user != null) {
            userIdLabel.setText("ID: " + user.getId());
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
            loginField.setText(user.getLogin());
            activeCheckBox.setSelected(user.getActive());
        }
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            user.setFirstName(firstNameField.getText().trim());
            user.setLastName(lastNameField.getText().trim());
            user.setEmail(emailField.getText().trim());
            user.setLogin(loginField.getText().trim());
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
