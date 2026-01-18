package com.springbootdemo.dto.request;

import java.time.LocalDate;

import com.springbootdemo.validator.BirthdayAnnotation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateRequest {
  private String username;
  private String firstname;
  private String lastname;

  @BirthdayAnnotation(min = 18)
  private LocalDate birthday;
}
