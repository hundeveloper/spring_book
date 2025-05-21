package com.example.book.university.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiErrorResponse {
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;
}