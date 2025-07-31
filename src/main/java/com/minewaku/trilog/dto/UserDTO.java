package com.minewaku.trilog.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@SuperBuilder
public class UserDTO extends BaseDTO {

    private String email;

    private String name;

    private LocalDate birthdate;

    private String phone;

    private String address;
    
    private String imageUrl;
    
    private String coverUrl;
    
    private boolean isActive;
    
    private boolean isEnabled;
    
    private boolean isLocked;
    
    private boolean isDeleted;
   
    private List<String> roles;
}
