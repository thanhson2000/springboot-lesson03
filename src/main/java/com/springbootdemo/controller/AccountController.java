package com.springbootdemo.controller;

import com.springbootdemo.common.Code;
import com.springbootdemo.common.ErrorResult;
import com.springbootdemo.common.Result;
import com.springbootdemo.dto.request.AccountBindingDto;
import com.springbootdemo.dto.request.AccountSaveRequest;
import com.springbootdemo.dto.response.APIResponseDto;
import com.springbootdemo.dto.response.AccountResponseDto;
import com.springbootdemo.enums.Role;
import com.springbootdemo.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AccountSaveRequest request){
        return ResponseEntity.ok(accountService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AccountSaveRequest request){
        return ResponseEntity.ok(accountService.login(request));
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAll(@Param("page") int page, @Param("size") int size){
        return ResponseEntity.ok(new Result<>(Code.SELECTED_SUCCESS,"查詢成功",accountService.getAll(page,size)));
    }

    @PostMapping("/binding")
    public ResponseEntity<?> binding(@RequestBody AccountBindingDto request){
        accountService.bindingRole(request.getUsername(),request.getRoles(),request.getPermissions());
        return ResponseEntity.ok(new ErrorResult(Code.UPDATED_SUCCESS,"修改成功"));
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username){
        return ResponseEntity.ok(new Result<>(Code.SELECTED_SUCCESS,"查詢成功",accountService.getByUsername(username)));
    }
}
