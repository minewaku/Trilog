package com.minewaku.trilog.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class MediaDTO implements Serializable {
    private Integer id;
    private String publicId;
    private String secureUrl;
}
