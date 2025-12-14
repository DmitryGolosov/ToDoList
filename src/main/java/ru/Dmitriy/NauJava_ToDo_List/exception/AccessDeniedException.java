package ru.Dmitriy.NauJava_ToDo_List.exception;

public class AccessDeniedException extends BaseException {
    public AccessDeniedException(Long userId, Long taskId) {
        super("Пользователь " + userId + " не имеет доступа к задаче " + taskId,
                "ACCESS_DENIED");
    }
}