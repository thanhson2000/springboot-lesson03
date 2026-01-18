package com.springbootdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.springbootdemo.dto.request.UserSaveRequest;
import com.springbootdemo.dto.request.UserUpdateRequest;
import com.springbootdemo.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserSaveRequest request);

  void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
