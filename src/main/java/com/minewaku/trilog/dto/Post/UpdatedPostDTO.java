package com.minewaku.trilog.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor

@SuperBuilder
public class UpdatedPostDTO {
    private String content;
}
