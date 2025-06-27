package com.vamospassar.respostabot.exception;


public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException() {
        super("Questão não encontrada");
    }

    public QuestionNotFoundException(String message) {
        super(message);
    }

}
