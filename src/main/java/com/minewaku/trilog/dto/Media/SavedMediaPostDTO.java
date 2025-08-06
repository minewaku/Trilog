package com.minewaku.trilog.dto.Media;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class SavedMediaPostDTO implements Serializable {
	private Integer id;
	private Integer postId;
	private String publicId;
	private String secureUrl;
	private Integer displayOrder;
	private Boolean isThumbnail;
}
