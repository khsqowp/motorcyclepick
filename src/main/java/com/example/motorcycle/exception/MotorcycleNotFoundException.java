// MotorcycleNotFoundException.java
package com.example.motorcycle.exception;

public class MotorcycleNotFoundException extends RuntimeException {
    public MotorcycleNotFoundException(String message) {
        super(message);
    }

    public MotorcycleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}