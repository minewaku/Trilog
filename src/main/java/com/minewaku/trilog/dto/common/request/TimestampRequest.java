package com.minewaku.trilog.dto.common.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimestampRequest {
	 private LocalDateTime timestamp;
}
