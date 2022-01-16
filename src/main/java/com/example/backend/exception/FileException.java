package com.example.backend.exception;

public class FileException extends BaseException {

    public FileException(String code) {
        super("file." + code);
    }

    public static FileException fileNull() {
        return new FileException("null");
    }

    public static FileException fileMaxSize() {
        return new FileException("Max.Size");
    }

    public static FileException fileUnSupported() {
        return new FileException("UnSupported");
    }
}
