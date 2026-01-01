package com.springbootdemo.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootdemo.controller.AccountController;
import com.springbootdemo.dto.request.AccountSaveRequest;
import com.springbootdemo.dto.response.AuthenticationResponseDto;
import com.springbootdemo.entity.Account;
import com.springbootdemo.service.AccountService;
import com.springbootdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
@TestPropertySource("/test.properties")
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountService accountService;

    private Account account;
    private AccountSaveRequest accountSaveRequest;

   // @BeforeEach
    void init(){
        account = new Account();
        // 預期結果
        account.setUsername("initiation");
        account.setPassword("1234qwer!");
        account.setPermissions(new HashSet<>());
        account.setRoles(new HashSet<>());
    }

    @Test
    public void testLogin() throws Exception {
        // request
        accountSaveRequest = new AccountSaveRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        accountSaveRequest.setUsername("admin");
        accountSaveRequest.setPassword("12333");

        String request = objectMapper.writeValueAsString(accountSaveRequest);

        Mockito.when(accountService.login(accountSaveRequest)).thenReturn(new AuthenticationResponseDto());

        mockMvc.perform(MockMvcRequestBuilders.post("/springboot/account/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
                .andExpectAll(MockMvcResultMatchers.status().is(200),MockMvcResultMatchers.jsonPath("$.status").value("true"));

    }
}
