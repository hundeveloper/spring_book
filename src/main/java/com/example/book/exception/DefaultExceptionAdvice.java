package com.example.book.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class DefaultExceptionAdvice {

    // 커스텀 비즈니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorObject> handleBusiness(BusinessException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorObject(e.getMessage()));
    }

    // @Valid 유효성 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleValidation(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("입력값이 올바르지 않습니다.");
        return ResponseEntity.badRequest()
                .body(new ErrorObject(errorMessage));
    }

    // 기타 예외 처리 (알 수 없는 서버 에러 등)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleGeneral(Exception e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorObject("서버 오류가 발생했습니다."));
    }
}