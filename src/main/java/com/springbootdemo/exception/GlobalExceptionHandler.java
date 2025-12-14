package com.springbootdemo.exception;

import com.springbootdemo.common.Code;
import com.springbootdemo.common.ErrorResult;
import com.springbootdemo.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ErrorResult> runTimeExceptionHandler(Exception exception){
        return ResponseEntity.badRequest().body(new ErrorResult(Code.RUNTIME_ERR,exception.getMessage()));
    }

    @ExceptionHandler(value = JwtException.class)
    ResponseEntity<ErrorResult> jwtExceptionHandler(JwtException exception){
        return ResponseEntity.badRequest().body(new ErrorResult(HttpServletResponse.SC_UNAUTHORIZED,exception.getMessage()));
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResult> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        return ResponseEntity.badRequest().body(new ErrorResult(Code.METHOD_ARGUMENT_NOT_VALID, exception.getFieldError().getDefaultMessage()));
    }
}
