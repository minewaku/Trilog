package com.minewaku.trilog.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "permission")
@SuperBuilder
public class Permission extends BaseEntity {
	
	@JsonManagedReference
	@OneToMany( mappedBy = "permission", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<RolePermission> rolePermissions;
	
	@Column(name = "name", length = 255, nullable = false, unique = true)
	@NotBlank(message = "Name is required")
	@NotNull(message = "Name cannot be null")
	private String name;
	
	@Column(name = "description", nullable = false, length = 255)
	@NotBlank(message = "Description is required")
	@NotNull(message = "Description cannot be null")
	private String description;

	@Column(name = "is_deleted", nullable = false)
	@NotNull(message = "isDeleted is required")
	private Boolean isDeleted;
	
    @PrePersist
	protected void onCreate() {
    	super.onCreate();
    	
		if (rolePermissions == null) {
			rolePermissions = new ArrayList<>();
		}
    	
		if (isDeleted == null) {
			isDeleted = false;			
		}
	}
}


