package com.springbootdemo.controller;

import com.springbootdemo.common.Code;
import com.springbootdemo.common.Result;
import com.springbootdemo.dto.request.UserSaveRequest;
import com.springbootdemo.dto.request.UserUpdateRequest;
import com.springbootdemo.entity.User;
import com.springbootdemo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value= "/save")
    public Result<User> usersave(@RequestBody @Valid UserSaveRequest request){
        return new Result<>(Code.INSERTED_SUCCESS,"添加成功",userService.save(request));
    }

    @GetMapping(value = "/getAll")
    public Result<List<User>> getAll(){
        return new Result<>(Code.SELECTED_SUCCESS,"查询成功",userService.getAll());
    }

    @GetMapping(value="/{userID}")
    public Result<User> getById(@PathVariable("userID") String id){
        return new Result<>(Code.SELECTED_SUCCESS,"查询成功", userService.getById(id));
    }

    @PutMapping("/{userID}")
    public Result<User> update(@PathVariable("userID")String id, @RequestBody UserUpdateRequest request){
        return new Result<>(Code.UPDATED_SUCCESS,"修改成功", userService.update(id,request));
    }

    @DeleteMapping("/{userID}")
    public void delete(@PathVariable("userID") String id){
        userService.delete(id);
    }
}
