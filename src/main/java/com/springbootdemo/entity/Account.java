package com.springbootdemo.entity;

import com.springbootdemo.enums.Permission;
import com.springbootdemo.validator.PasswordAnnotation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import com.springbootdemo.enums.Role;

import java.util.Set;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(unique = true, nullable = false)
    String username;

    String password;

    @ElementCollection(fetch = FetchType.EAGER)
            @CollectionTable(name = "account_roles",joinColumns = @JoinColumn(name = "account_id"))
            @Enumerated(EnumType.STRING)
            @Column(name = "role")
    Set<Role> roles;

    @ElementCollection(fetch = FetchType.EAGER)
            @CollectionTable(name = "account_permissions", joinColumns = @JoinColumn(name = "account_id"))
            @Enumerated(EnumType.STRING)
            @Column(name = "permission")
    Set<Permission> permissions;

}
