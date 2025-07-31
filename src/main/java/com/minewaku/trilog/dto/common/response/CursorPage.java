package com.minewaku.trilog.dto.common.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class CursorPage<T> {
    private Integer after;
    private Integer before;
    private Integer limit;
    private Integer total;
	private List<T> records;
	
	public boolean hasNextPage() {
		return limit > records.size();
	}
	
	public boolean hasPreviousPage() {
		return before != null;
	}
}
