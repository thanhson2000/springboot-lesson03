package com.springbootdemo.service;

import com.springbootdemo.common.TokenHandler;
import com.springbootdemo.dto.request.AccountSaveRequest;
import com.springbootdemo.dto.response.AccountResponseDto;
import com.springbootdemo.dto.response.AuthenticationResponseDto;
import com.springbootdemo.entity.Account;
import com.springbootdemo.enums.Permission;
import com.springbootdemo.enums.Role;
import com.springbootdemo.mapper.AccountMapper;
import com.springbootdemo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public Account register(AccountSaveRequest request){
        Optional<Account> byUsername = accountRepository.findByUsername(request.getUsername());
        if (! byUsername.isEmpty()){
            throw new RuntimeException("帳號已存在");
        }
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        return accountRepository.save(account);
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
}
