package ru.Dmitriy.NauJava_ToDo_List.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import ru.Dmitriy.NauJava_ToDo_List.entity.Priority;
import ru.Dmitriy.NauJava_ToDo_List.entity.Task;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDTO {
    private Long id;

    @NotBlank(message = "Описание задачи обязательно")
    @Size(min = 3, max = 500, message = "Описание должно быть от 3 до 500 символов")
    private String description;

    @NotNull(message = "Приоритет обязателен")
    private Priority priority;

    private Boolean completed;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime completedAt;

    private Long userId;
    private String userFullName;

    public TaskDTO() {}

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.description = task.getDescription();
        this.priority = task.getPriority();
        this.completed = task.getCompleted(); // Используем getCompleted()
        this.createdAt = task.getCreatedAt(); // Из BaseEntity
        this.updatedAt = task.getUpdatedAt(); // Из BaseEntity
        this.completedAt = task.getCompletedAt();

        if (task.getUser() != null) {
            this.userId = task.getUser().getId();
            this.userFullName = task.getUser().getFullName();
        }
    }

    public static TaskDTO fromEntity(Task task) {
        return new TaskDTO(task);
    }

    public static List<TaskDTO> fromEntities(List<Task> tasks) {
        return tasks.stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getStatus() {
        return completed ? "Выполнена" : "Активна";
    }

    public String getPriorityDisplayName() {
        return priority != null ? priority.getDisplayName() : "Не указан";
    }
}
