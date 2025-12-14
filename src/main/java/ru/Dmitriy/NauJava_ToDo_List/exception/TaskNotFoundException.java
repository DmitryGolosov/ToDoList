package ru.Dmitriy.NauJava_ToDo_List.exception;

public class TaskNotFoundException extends BaseException {
    public TaskNotFoundException(Long taskId) {
        super("Задача с ID " + taskId + " не найдена", "TASK_NOT_FOUND");
    }
}
