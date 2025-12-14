package ru.Dmitriy.NauJava_ToDo_List.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterDTO {
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 30, message = "Логин должен быть от 3 до 30 символов")
    private String login;

    @NotBlank(message = "Пароль обязателен")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Пароль должен содержать минимум 8 символов, заглавные и строчные буквы и цифры"
    )
    private String password;

    // Геттеры и сеттеры
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    }
