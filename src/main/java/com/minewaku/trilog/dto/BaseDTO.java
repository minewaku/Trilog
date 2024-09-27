package com.minewaku.trilog.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseDTO {
    private Integer id;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}




