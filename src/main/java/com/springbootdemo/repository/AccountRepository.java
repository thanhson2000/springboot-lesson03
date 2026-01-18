package com.springbootdemo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springbootdemo.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
  Optional<Account> findByUsername(String username);

  @Query("SELECT account FROM Account account")
  Page<Account> getAll(int page, int size, Pageable pageable);
}
