package com.example.backend.exception;

public class UserException extends BaseException {

    public UserException(String code) {
        super("user." + code);
    }

    public static UserException emailNull() {
        return new UserException("register.email.null");
    }

    public static UserException requestNull() {
        return new UserException("register.request.null");
    }

    public static UserException passNull() {
        return new UserException("register.pass.null");
    }

    // Create
    public static UserException createEmailNull() {
        return new UserException("create.email.null");
    }

    public static UserException createEmailDuplicated() {
        return new UserException("create.email.Duplicated");
    }

    public static UserException createPassNull() {
        return new UserException("create.Pass.null");
    }

    public static UserException createNameNull() {
        return new UserException("create.Name.null");
    }

    // Login
    public static UserException loginFailEmailNotFound() {
        return new UserException("Login.Fail");
    }

    public static UserException loginFailPassIncorrect() {
        return new UserException("Login.Fail");
    }

    public static UserException notFound() {
        return new UserException("not.Found");
    }

    public static UserException unAuthentication() {
        return new UserException("unauthentication");
    }
}
