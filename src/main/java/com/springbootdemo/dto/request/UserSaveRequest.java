package com.springbootdemo.dto.request;

import java.time.LocalDate;

import com.springbootdemo.validator.BirthdayAnnotation;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSaveRequest {
  @Size(min = 8, message = "帳號至少8個字符")
  String username;

  String firstname;
  String lastname;

  @BirthdayAnnotation(min = 18)
  LocalDate birthday;
}
