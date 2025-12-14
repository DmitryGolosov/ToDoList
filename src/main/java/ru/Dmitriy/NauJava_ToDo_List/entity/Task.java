package ru.Dmitriy.NauJava_ToDo_List.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

    @NotBlank(message = "Описание задачи обязательно")
    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Task() {
        super();
    }

    public Task(String description, Priority priority, User user) {
        this();
        this.description = description;
        this.priority = priority;
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
        if (completed) {
            this.completedAt = LocalDateTime.now();
        } else {
            this.completedAt = null;
        }
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatusText() {
        return completed ? "Выполнена" : "Активна";
    }

    public String getPriorityColor() {
        switch (priority) {
            case HIGH: return "#ff4444";
            case MEDIUM: return "#ffbb33";
            case LOW: return "#00C851";
            default: return "#cccccc";
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", completed=" + completed +
                ", createdAt=" + getCreatedAt() +
                ", user=" + (user != null ? user.getFullName() : "null") +
                '}';
    }
}
