package com.minewaku.trilog.dto.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenicationRequest {

    private String email;
    private String password;
}
