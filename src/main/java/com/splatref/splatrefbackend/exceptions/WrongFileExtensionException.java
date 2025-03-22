package com.splatref.splatrefbackend.exceptions;

public class WrongFileExtensionException extends IllegalArgumentException {
    public WrongFileExtensionException(String message) {
        super(message);
    }
}
