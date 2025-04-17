package com.minewaku.trilog.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany(fetch = FetchType.LAZY, 
                mappedBy = "permissions")
    private List<Role> roles;
	
	@Column(name = "name", length = 255, unique = true)
	@NotBlank(message = "Name is required")
	private String name;
	
	@Column(name = "description", length = 255)
	@NotBlank(message = "Description is required")
	private String description;

	@Column(name = "is_deleted")
	@NotNull(message = "isDeleted is required")
	private Boolean isDeleted;
	
    @PrePersist
	protected void onCreate() {
    	super.onCreate();
    	roles = new ArrayList<>();
		isDeleted = false;
	}
}


