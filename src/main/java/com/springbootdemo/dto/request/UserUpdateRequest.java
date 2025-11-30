package com.springbootdemo.dto.request;

import com.springbootdemo.validator.BirthdayAnnotation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserUpdateRequest
{
    private String username;
    private String firstname;
    private String lastname;
    @BirthdayAnnotation(min = 18)
    private LocalDate birthday;

}
