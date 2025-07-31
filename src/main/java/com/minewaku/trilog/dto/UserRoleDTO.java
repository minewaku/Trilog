package com.minewaku.trilog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleDTO {
	private Integer userId;
	private Integer roleId;
	private String roleName;
	private String userName;
	
}
