package ru.Dmitriy.NauJava_ToDo_List.exception;

public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
}