package com.springbootdemo.controller;

import com.springbootdemo.dto.request.AuthenticRequest;
import com.springbootdemo.service.AuthenticService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticController {

    @Autowired
    private AuthenticService authenticService;

    @PostMapping("/checkPassword")
    public ResponseEntity<?> checkPassword(@RequestBody @Valid AuthenticRequest request){
        return ResponseEntity.ok(authenticService.isPassword(request) ? "正確" : "錯誤");
    }
}
