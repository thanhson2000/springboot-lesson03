package com.springbootdemo.dto.response;

import java.util.Set;

import com.springbootdemo.enums.Permission;
import com.springbootdemo.enums.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponseDto {
  String username;
  Set<Role> roles;
  Set<Permission> permissions;
}
