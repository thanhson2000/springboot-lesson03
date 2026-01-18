package com.springbootdemo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.springbootdemo.dto.request.AccountSaveRequest;
import com.springbootdemo.dto.response.AccountResponseDto;
import com.springbootdemo.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
  Account toAccount(AccountSaveRequest request);

  @Mapping(target = "username", source = "username")
  @Mapping(target = "roles", source = "roles")
  @Mapping(target = "permissions", source = "permissions")
  AccountResponseDto toResponseDto(Account account);

  List<AccountResponseDto> toListDto(List<Account> list);

  default Page<AccountResponseDto> toPageDto(Page<Account> page) {
    return page.map(this::toResponseDto);
  }
}
