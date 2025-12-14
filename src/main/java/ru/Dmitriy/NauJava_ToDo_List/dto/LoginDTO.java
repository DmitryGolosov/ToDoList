package ru.Dmitriy.NauJava_ToDo_List.dto;

import ru.Dmitriy.NauJava_ToDo_List.dto.RegisterDTO;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginDTO {

    @NotBlank(message = "Логин или email обязателен")
    private String username; // Может быть login или email

    @NotBlank(message = "Пароль обязателен")
    private String password;

    // Геттеры и сеттеры
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
