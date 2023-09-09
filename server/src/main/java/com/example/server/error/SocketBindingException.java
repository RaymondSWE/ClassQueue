package com.example.server.error;

public class SocketBindingException extends RuntimeException {
    public SocketBindingException(String message) {
        super(message);
    }
}
