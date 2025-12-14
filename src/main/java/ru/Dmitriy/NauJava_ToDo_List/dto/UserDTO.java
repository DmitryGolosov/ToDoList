package ru.Dmitriy.NauJava_ToDo_List.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import ru.Dmitriy.NauJava_ToDo_List.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String firstName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 30, message = "Логин должен быть от 3 до 30 символов")
    private String login;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Пароль должен содержать минимум 8 символов, заглавные и строчные буквы и цифры"
    )
    private String password;

    private Boolean active;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    private Integer taskCount;
    private List<TaskDTO> tasks;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.active = user.getActive();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();

        if (user.getTasks() != null) {
            this.taskCount = user.getTasks().size();
            this.tasks = user.getTasks().stream()
                    .map(TaskDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public static UserDTO fromEntity(User user) {
        return new UserDTO(user);
    }

    public static List<UserDTO> fromEntities(List<User> users) {
        return users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Integer getTaskCount() { return taskCount; }
    public void setTaskCount(Integer taskCount) { this.taskCount = taskCount; }

    public List<TaskDTO> getTasks() { return tasks; }
    public void setTasks(List<TaskDTO> tasks) { this.tasks = tasks; }

    public String getFullName() {
        return lastName + " " + firstName;
    }
}
