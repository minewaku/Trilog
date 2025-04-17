package com.minewaku.trilog.dto;

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
public class PermissionDTO extends BaseDTO {
    private String name;
    private String description;
    private Boolean isDeleted;
}
