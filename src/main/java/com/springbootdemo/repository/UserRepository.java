package com.springbootdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springbootdemo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  // 通過Username檢查數據是否已存在
  boolean existsByUsername(String username);
}
