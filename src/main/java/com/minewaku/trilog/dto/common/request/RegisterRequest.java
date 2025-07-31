package com.minewaku.trilog.dto.common.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor

@SuperBuilder
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private LocalDate birthdate;
    private String phone;
    private String address;
}


