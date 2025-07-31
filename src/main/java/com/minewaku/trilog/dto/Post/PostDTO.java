package com.minewaku.trilog.dto.Post;

import com.minewaku.trilog.dto.BaseDTO;
import com.minewaku.trilog.dto.Media.MediaPostDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@SuperBuilder
public class PostDTO extends BaseDTO {

	private Integer userId;
	private String username;
	private MediaPostDTO[] thumbnails;
    private String content;
    private Integer status;
    private Integer likeCount;
    private Integer viewCount;
    private Integer commentCount;
    private Double lat;
    private Double lon;
}
