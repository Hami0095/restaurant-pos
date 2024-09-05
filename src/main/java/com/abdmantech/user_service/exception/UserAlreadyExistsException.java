package com.abdmantech.user_service.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
