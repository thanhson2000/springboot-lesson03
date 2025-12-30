package com.springbootdemo.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.springbootdemo.common.TokenHandler;
import com.springbootdemo.dto.request.AccountSaveRequest;
import com.springbootdemo.dto.response.AccountResponseDto;
import com.springbootdemo.dto.response.AuthenticationResponseDto;
import com.springbootdemo.entity.Account;
import com.springbootdemo.entity.InValidToken;
import com.springbootdemo.enums.Permission;
import com.springbootdemo.enums.Role;
import com.springbootdemo.mapper.AccountMapper;
import com.springbootdemo.repository.AccountRepository;
import com.springbootdemo.repository.InValidTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InValidTokenRepository inValidTokenRepository;

    public Account register(AccountSaveRequest request){

        try {
            Optional<Account> byUsername = accountRepository.findByUsername(request.getUsername());
            if (byUsername.isPresent()) {
                throw new RuntimeException("帳號已存在");
            }
            Account account = accountMapper.toAccount(request);
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            account.setPermissions(new HashSet<>());
            account.setRoles(new HashSet<>());
            return accountRepository.save(account);
        } catch (Exception e) {
            System.err.println("=== 錯誤詳情 ===");
            System.err.println("錯誤類型: " + e.getClass().getName());
            System.err.println("錯誤消息: " + e.getMessage());
            e.printStackTrace();

            // 查找根本原因
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
                System.err.println("根本原因: " + cause.getClass().getName() + " - " + cause.getMessage());
            }

            throw new RuntimeException("註冊失敗: " + e.getMessage(), e);
        }
    }

    public AuthenticationResponseDto login(AccountSaveRequest request){
        Optional<Account> account = accountRepository.findByUsername(request.getUsername());
        if (account.isEmpty()){
            throw new RuntimeException("此帳號不存在");
        }
        boolean matches = passwordEncoder.matches(request.getPassword(), account.get().getPassword());
        if (! matches){
            return new AuthenticationResponseDto(matches,null);
        }
        String token = tokenHandler.generate(account.get());
        return new AuthenticationResponseDto(matches,token);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<AccountResponseDto> getAll(int page, int size){
        Pageable pageable =  PageRequest.of(page,size,Sort.by("id").ascending());
        return accountMapper.toPageDto(accountRepository.getAll(page,size,pageable));
    }

    @PreAuthorize("hasRole('ADMIN') && hasAuthority('ACCOUNT_POST')")
    public void bindingRole(String username, Set<Role> request_roles, Set<Permission> request_permission){
        Optional<Account> byAccount = accountRepository.findByUsername(username);
        if(byAccount.isEmpty()){
            throw new RuntimeException("沒查詢到此帳號");
        }

        Account account = byAccount.get();
        account.setRoles(request_roles);
        account.setPermissions(request_permission);
        accountRepository.save(account);
    }

    @PreAuthorize("hasRole('ADMIN') && hasAuthority('ACCOUNT_POST')")
    public AccountResponseDto getByUsername(String username){
        Optional<Account> byAccount = accountRepository.findByUsername(username);
        if (byAccount.isEmpty()){
            throw  new RuntimeException("查詢不到此用戶");
        }
        Account account = byAccount.get();
        return accountMapper.toResponseDto(account);
    }

    public void logout(String token) throws ParseException {
        JWTClaimsSet jwtClaimsSet = tokenHandler.getSignedJWT(token).getJWTClaimsSet();
        String jwtid = jwtClaimsSet.getJWTID();
        Date expirationTime = jwtClaimsSet.getExpirationTime();
        inValidTokenRepository.save(new InValidToken(jwtid,expirationTime));
    }

    public String refreshToken(String token) throws ParseException {
        SignedJWT signedJWT = tokenHandler.getSignedJWT(token);
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        String jwtId = jwtClaimsSet.getJWTID();
        Date expirationTime = jwtClaimsSet.getExpirationTime();
        inValidTokenRepository.save(new InValidToken(jwtId,expirationTime));

        String username = signedJWT.getJWTClaimsSet().getSubject();

        Account account = accountRepository.findByUsername(username).orElseThrow();
        return tokenHandler.generate(account);
    }
}
