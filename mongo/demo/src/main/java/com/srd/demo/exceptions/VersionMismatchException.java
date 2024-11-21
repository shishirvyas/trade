package com.srd.demo.exceptions;

public class VersionMismatchException extends RuntimeException {
    public VersionMismatchException(String message) {
        super(message);
    }
}

