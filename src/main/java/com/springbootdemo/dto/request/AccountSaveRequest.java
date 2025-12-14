package com.springbootdemo.dto.request;

import com.springbootdemo.validator.PasswordAnnotation;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountSaveRequest {
    String username;

    @PasswordAnnotation
    String password;
}
