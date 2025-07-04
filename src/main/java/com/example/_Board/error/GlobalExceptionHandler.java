package com.example._Board.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessException> BusinessExceptionHandler(BusinessException e) {
        return ResponseEntity.badRequest().body(new BusinessException(e.getErrorCode()));
    }
}
