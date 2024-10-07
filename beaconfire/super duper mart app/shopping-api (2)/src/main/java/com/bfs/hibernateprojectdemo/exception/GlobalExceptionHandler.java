package com.bfs.hibernateprojectdemo.exception;

import com.bfs.hibernateprojectdemo.domain.dto.common.DataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(DataResponse.<Object>builder()
                .success(false)
                .message("system error: " + e.getMessage())
                .build());
    }

}
