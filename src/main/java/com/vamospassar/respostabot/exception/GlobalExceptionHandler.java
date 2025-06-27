package com.vamospassar.respostabot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, String> handleUserNotFound(UserNotFoundException exception) {
        return Map.of("error", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(QuestionNotFoundException.class)
    public Map<String, String> handlerQuestionNotFoundException(QuestionNotFoundException exception) {
        return Map.of("error", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Map<String, String> handleUserAlreadyExistException(UserAlreadyExistsException exception) {
        return Map.of("error", exception.getMessage());
    }


    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    @ExceptionHandler(FreeTimeException.class)
    public Map<String, String> handlerFreeTimeException(FreeTimeException exception) {
        return Map.of("error", exception.getMessage());
    }

}
