// MotorcycleValidationException.java
package com.example.motorcyclepick.exception;

public class MotorcycleValidationException extends RuntimeException {
    public MotorcycleValidationException(String message) {
        super(message);
    }

    public MotorcycleValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}