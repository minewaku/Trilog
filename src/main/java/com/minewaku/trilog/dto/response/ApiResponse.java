package com.minewaku.trilog.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class ApiResponse {

    private String status;

    private int code;

    private String message;

    private Object data;
}
