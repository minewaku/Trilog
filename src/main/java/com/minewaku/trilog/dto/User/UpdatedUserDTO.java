package com.minewaku.trilog.dto.User;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedUserDTO {
    private String name;

    private LocalDate birthdate;

    private String phone;

    private String address;
}
