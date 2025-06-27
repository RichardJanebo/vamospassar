package com.vamospassar.respostabot.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(){
        super("Usuario ja existe");
    }
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
