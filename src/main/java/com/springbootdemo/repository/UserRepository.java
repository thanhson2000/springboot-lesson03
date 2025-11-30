package com.springbootdemo.repository;

import com.springbootdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // 通過Username檢查數據是否已存在
    boolean existsByUsername(String username);
}
