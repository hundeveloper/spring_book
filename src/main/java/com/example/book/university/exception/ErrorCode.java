package com.example.book.university.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 학과를 찾을 수 없습니다."),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 학생을 찾을 수 없습니다."),
    DUPLICATE_DEPARTMENT_CODE(HttpStatus.CONFLICT, "이미 존재하는 학과 코드입니다."),
    DUPLICATE_STUDENT_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 학번입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DEPARTMENT_HAS_STUDENTS(HttpStatus.CONFLICT, "학생이 등록된 학과는 삭제할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}