package com.vamospassar.respostabot.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("Usuario não encontrado");
    }

    public UserNotFoundException(String message){
        super(message);
    }
}
