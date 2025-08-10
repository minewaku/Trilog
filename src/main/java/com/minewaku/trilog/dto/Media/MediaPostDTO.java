package com.minewaku.trilog.dto.Media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class MediaPostDTO {
	private Integer id;
	private String publicId;
	private String secureUrl;
	private Integer displayOrder;
	private Boolean isThumbnail;
}
