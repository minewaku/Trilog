package com.minewaku.trilog.dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class CommentDTO {
	private Integer id;
	private String content;
	private Integer postId;
	private Integer userId;
}
