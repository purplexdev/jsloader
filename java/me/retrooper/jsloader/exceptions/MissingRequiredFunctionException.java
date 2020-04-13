package me.retrooper.jsloader.exceptions;

public class MissingRequiredFunctionException extends RuntimeException {
    public MissingRequiredFunctionException(String info) {
        super(info);
    }
}

