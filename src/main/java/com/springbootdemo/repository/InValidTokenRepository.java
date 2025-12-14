package com.springbootdemo.repository;

import com.springbootdemo.entity.InValidToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InValidTokenRepository extends JpaRepository<InValidToken, String> {
}
