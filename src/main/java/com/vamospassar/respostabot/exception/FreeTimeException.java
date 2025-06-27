package com.vamospassar.respostabot.exception;

public class FreeTimeException extends RuntimeException {
    public FreeTimeException() {
        super("Seu período de teste gratuito expirou. Que tal assinar nosso serviço para continuar usando? Sua assinatura também ajuda a apoiar o desenvolvimento do projeto!");
    }

    public FreeTimeException(String message){
        super(message);
    }
}
