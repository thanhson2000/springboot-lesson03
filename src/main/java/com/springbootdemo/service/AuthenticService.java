package com.springbootdemo.service;

import com.springbootdemo.dto.request.AuthenticRequest;
import com.springbootdemo.entity.Account;
import com.springbootdemo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticService {

    @Autowired
    private AccountRepository accountRepository;

    public boolean isPassword(AuthenticRequest request){
        var username = accountRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("查詢不到account"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassword(), username.getPassword());
    }
}
