package com.minewaku.trilog.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewDTO {
	private Integer userId;
	private Integer postId;
	private LocalDateTime createdDate;
}
