package com.minewaku.trilog.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class PaginationResponse<T> {
	private int page;
	private int limit;
	private List<String> sort;
	private String order;
	private List<T> records;
}
