package com.minewaku.trilog.dto.Role;

import com.minewaku.trilog.dto.BaseDTO;

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
public class RoleDTO extends BaseDTO {
    private String name;
    private String description; 
}
