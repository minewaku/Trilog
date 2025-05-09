package com.minewaku.trilog.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Cursor {
	private Integer after;
	private Integer before;
	private Integer limit;
}
