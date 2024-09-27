package com.minewaku.trilog.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.minewaku.trilog.util.BuilderUtil.FieldBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Table(name = "role")
@SuperBuilder
public class Role extends BaseEntity {

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	private List<User> users = new ArrayList<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_permission", 
				joinColumns = @JoinColumn(name = "role_id"), 
				inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions = new ArrayList<>();

	@Column(name = "name", length = 255, unique = true)
	@NotBlank(message = "Name is required")
	private String name;
	
	@Column(name = "description", length = 255)
	@NotBlank(message = "Description is required")
	private String description;

	@Column(name = "active")
	@NotNull(message = "Active is required")
	@FieldBuilder
	private Boolean active = true;

	public List<SimpleGrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Permission permission : permissions) {
			authorities.add(new SimpleGrantedAuthority(permission.getName()));
		}

		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name));
		return authorities;
	}
}
