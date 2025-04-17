package com.minewaku.trilog.dto.response;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class StatusResponse {
	
    private String message;
    private ZonedDateTime timestamp;
}
