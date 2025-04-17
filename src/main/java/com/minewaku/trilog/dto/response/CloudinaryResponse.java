package com.minewaku.trilog.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class CloudinaryResponse {

    private String publicId;

    private String secureUrl;
}
