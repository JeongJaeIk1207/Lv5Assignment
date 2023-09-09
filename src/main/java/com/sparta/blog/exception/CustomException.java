package com.sparta.blog.exception;

public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message); // 부모 클래스로 전달
    }
}
