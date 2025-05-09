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
public class PostDTO extends BaseDTO {

	private Integer userId;
	private String username;
    private String content;
    private Integer status;
    private Integer likes;
    private Integer views;
    private Integer comments;
    private Double lat;
    private Double lon;
}
