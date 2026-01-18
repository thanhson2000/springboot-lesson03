package com.springbootdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootdemo.common.TokenHandler;
import com.springbootdemo.dto.request.UserSaveRequest;
import com.springbootdemo.dto.request.UserUpdateRequest;
import com.springbootdemo.entity.User;
import com.springbootdemo.mapper.UserMapper;
import com.springbootdemo.repository.AccountRepository;
import com.springbootdemo.repository.InValidTokenRepository;
import com.springbootdemo.repository.UserRepository;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private UserMapper userMapper;
  @Autowired private TokenHandler tokenHandler;
  @Autowired private InValidTokenRepository inValidTokenRepository;
  @Autowired private AccountRepository accountRepository;

  public User save(UserSaveRequest request) {
    if (findByUserName(request.getUsername())) {
      throw new RuntimeException("User exists");
    }
    User user = userMapper.toUser(request);
    return userRepository.save(user);
  }

  public List<User> getAll() {
    return userRepository.findAll();
  }

  public User getById(String id) {
    return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
  }

  public User update(String id, UserUpdateRequest request) {
    User user = getById(id);
    userMapper.updateUser(user, request);
    return userRepository.save(user);
  }

  public void delete(String id) {
    userRepository.deleteById(id);
  }

  public boolean findByUserName(String username) {
    return userRepository.existsByUsername(username);
  }
}
