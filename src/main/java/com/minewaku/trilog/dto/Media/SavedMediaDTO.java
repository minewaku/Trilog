package com.minewaku.trilog.dto.Media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class SavedMediaDTO {
	private String publicId;
	private String secureUrl;
}
