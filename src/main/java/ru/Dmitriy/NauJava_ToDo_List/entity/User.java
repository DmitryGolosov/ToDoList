package ru.Dmitriy.NauJava_ToDo_List.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")

public class User extends BaseEntity {

    @NotBlank(message = "Фамилия обязательна")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email обязателен")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Логин обязателен")
    @Column(unique = true, nullable = false)
    private String login;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Пароль должен содержать заглавные и строчные буквы и цифры"
    )
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    private transient LongProperty idProperty;
    private transient StringProperty fullNameProperty;
    private transient StringProperty emailProperty;
    private transient StringProperty loginProperty;
    private transient BooleanProperty activeProperty;

    public User() {
        super();
    }

    public User(String lastName, String firstName, String email, String login, String password) {
        this();
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public LongProperty idProperty() {
        if (idProperty == null) {
            idProperty = new SimpleLongProperty(getId());
        }
        return idProperty;
    }

    public StringProperty fullNameProperty() {
        if (fullNameProperty == null) {
            fullNameProperty = new SimpleStringProperty(getFullName());
        }
        return fullNameProperty;
    }

    public StringProperty emailProperty() {
        if (emailProperty == null) {
            emailProperty = new SimpleStringProperty(email);
        }
        return emailProperty;
    }

    public StringProperty loginProperty() {
        if (loginProperty == null) {
            loginProperty = new SimpleStringProperty(login);
        }
        return loginProperty;
    }

    public BooleanProperty activeProperty() {
        if (activeProperty == null) {
            activeProperty = new SimpleBooleanProperty(getActive());
        }
        return activeProperty;
    }

    public void updateProperties() {
        if (idProperty != null) idProperty.set(getId());
        if (fullNameProperty != null) fullNameProperty.set(getFullName());
        if (emailProperty != null) emailProperty.set(email);
        if (loginProperty != null) loginProperty.set(login);
        if (activeProperty != null) activeProperty.set(getActive());
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setUser(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setUser(null);
    }

    @Override
    public void setActive(Boolean active) {
        super.setActive(active);
        if (activeProperty != null) activeProperty.set(active);
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
        if (idProperty != null) idProperty.set(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", active=" + getActive() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
