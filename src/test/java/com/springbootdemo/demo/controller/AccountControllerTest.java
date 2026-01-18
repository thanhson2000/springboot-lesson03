package com.springbootdemo.demo.controller;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootdemo.dto.request.AccountSaveRequest;
import com.springbootdemo.entity.Account;
import com.springbootdemo.mapper.AccountMapper;
import com.springbootdemo.repository.AccountRepository;
import com.springbootdemo.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
@TestPropertySource("/test.properties")
@Transactional
public class AccountControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private AccountService accountService;

  @Autowired private AccountMapper accountMapper;

  private Account account;
  private AccountSaveRequest accountSaveRequest;
  @Autowired private AccountRepository accountRepository;

  @BeforeEach
  void init() {
    accountRepository.deleteAll();

    // 初始化測試數據
    account = new Account();
    account.setUsername("admin");
    account.setPassword(passwordEncoder.encode("1234qwer!"));
    account.setPermissions(new HashSet<>());
    account.setRoles(new HashSet<>());

    // 將測試數據存儲到H2內存數據庫
    accountRepository.save(account);
  }

  @Test
  public void testLogin_Success() throws Exception {

    // request
    accountSaveRequest = new AccountSaveRequest();

    ObjectMapper objectMapper = new ObjectMapper();
    accountSaveRequest.setUsername("admin");
    accountSaveRequest.setPassword("1234qwer!");

    String request = objectMapper.writeValueAsString(accountSaveRequest);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
        .andExpectAll(
            MockMvcResultMatchers.status().is(200),
            MockMvcResultMatchers.jsonPath("$.status").value("true"),
            MockMvcResultMatchers.jsonPath("$.token").exists());

    log.info("登錄測試成功");
  }

  @Test
  void testLogin_WrongUsername() throws Exception {
    accountSaveRequest = new AccountSaveRequest("test01", "1234qwer!");
    ObjectMapper objectMapper = new ObjectMapper();
    String request = objectMapper.writeValueAsString(accountSaveRequest);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
        .andExpectAll(
            MockMvcResultMatchers.status().is(400),
            MockMvcResultMatchers.jsonPath("$.code").value(5000),
            MockMvcResultMatchers.jsonPath("$.msg").value("此帳號不存在"));

    log.info("當username錯誤時，登錄失敗 ---> 測試通過");
  }

  @Test
  void testLogin_InvalidPasswordLength() throws Exception {
    accountSaveRequest = new AccountSaveRequest("admin", "123456");
    ObjectMapper objectMapper = new ObjectMapper();
    String request = objectMapper.writeValueAsString(accountSaveRequest);
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
        .andExpectAll(
            MockMvcResultMatchers.status().isBadRequest(),
            MockMvcResultMatchers.jsonPath("$.code").value(5001),
            MockMvcResultMatchers.jsonPath("$.msg").value("密碼長度需要在8～16個字符"));
    log.info("登錄password長度不符合 登錄失敗 ---> 測試通過");
  }

  @Test
  void testLogin_PasswordFullOfNumber() throws Exception {
    accountSaveRequest = new AccountSaveRequest("admin", "123456789");
    ObjectMapper objectMapper = new ObjectMapper();
    String request = objectMapper.writeValueAsString(accountSaveRequest);
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
        .andExpectAll(
            MockMvcResultMatchers.status().isBadRequest(),
            MockMvcResultMatchers.jsonPath("$.code").value(5001),
            MockMvcResultMatchers.jsonPath("$.msg").value("密碼需要包含至少一個英文字母"));
    log.info("登錄password只有數字 登錄失敗 ---> 測試通過");
  }

  @WithMockUser(roles = "ADMIN")
  @Test
  void testGetAll_WithADMIN_Success() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/account/getall").param("page", "0").param("size", "5"))
        .andExpectAll(
            MockMvcResultMatchers.status().isOk(),
            MockMvcResultMatchers.jsonPath("$.code").value(1000),
            MockMvcResultMatchers.jsonPath("$.data").isMap());
    log.info("ADMIN權限獲取列表成功");
  }
}
