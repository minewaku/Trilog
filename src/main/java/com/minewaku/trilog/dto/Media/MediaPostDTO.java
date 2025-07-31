package com.minewaku.trilog.dto.Media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MediaPostDTO {
	private String publicId;
	private String secureUrl;
	private Integer displayOrder;
	private Boolean isThumbnail;
}
