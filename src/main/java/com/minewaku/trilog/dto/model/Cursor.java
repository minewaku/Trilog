package com.minewaku.trilog.dto.model;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cursor implements Serializable {
	@NotNull(message = "Cursor cannot be null")
    private Integer cursor;
    
    @Min(value = 1, message = "Size must be greater than 0")
    private int size = 10;    
}