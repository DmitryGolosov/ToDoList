package ru.Dmitriy.NauJava_ToDo_List.service;

import ru.Dmitriy.NauJava_ToDo_List.entity.Task;
import ru.Dmitriy.NauJava_ToDo_List.entity.User;
import ru.Dmitriy.NauJava_ToDo_List.repository.TaskRepository;
import ru.Dmitriy.NauJava_ToDo_List.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    public Task createTask(Long userId, String description, String priority) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Пользователь не найден");
        }

        Task task = new Task();
        task.setDescription(description);
        task.setPriority(ru.Dmitriy.NauJava_ToDo_List.entity.Priority.valueOf(priority));
        task.setUser(userOpt.get());

        return taskRepository.save(task);
    }

    public List<Task> getUserTasks(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Page<Task> getTasksPage(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findByUserId(userId, pageable);
    }

    public void completeTask(Long taskId) {
        taskRepository.markTaskAsCompleted(taskId);
    }

    public List<Task> searchTasks(String keyword, Long userId, Boolean completed) {
        return taskRepository.findTasksByCriteria(userId, completed, null, keyword);
    }

    public void cleanupOldTasks() {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        taskRepository.deleteOldCompletedTasks(monthAgo);
    }
}
