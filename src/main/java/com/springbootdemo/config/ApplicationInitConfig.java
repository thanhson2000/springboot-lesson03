package com.springbootdemo.config;

import com.springbootdemo.entity.Account;
import com.springbootdemo.enums.Permission;
import com.springbootdemo.enums.Role;
import com.springbootdemo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ConditionalOnProperty(prefix = "spring", value = "datasource.driverClassName", havingValue = "com.mysql.cj.jdbc.Driver")
    @Bean
    ApplicationRunner applicationRunner(AccountRepository accountRepository){
        return args ->{
            Optional<Account> username = accountRepository.findByUsername("admin");
            Account account = username.get();
            if (account == null){
                Account admin = new Account();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456qwer!"));
                Set<Role> roles = new HashSet<>();
                roles.add(Role.ADMIN);
                admin.setRoles(roles);
                accountRepository.save(admin);
            }else if(account.getRoles().isEmpty()){
                Set<Role> roles = new HashSet<>();
                roles.add(Role.ADMIN);
                account.setRoles(roles);
                accountRepository.save(account);
            }else if (account.getPermissions().isEmpty()){
                Set<Permission> permissions = new HashSet<>();
                permissions.add(Permission.ACCOUNT_POST);
                account.setPermissions(permissions);
                accountRepository.save(account);
            }
        };
    }
}
