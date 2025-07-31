package com.minewaku.trilog.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "role")
@SuperBuilder
public class Role extends BaseEntity {

	@JsonManagedReference
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<RolePermission> rolePermissions;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<UserRole> userRoles;

    @Column(name = "name", length = 255, nullable = false, unique = true)
    @NotBlank(message = "Name is required")
    @NotNull(message = "Name cannot be null")
    private String name;

    @Column(name = "description", length = 255, nullable = false)
    @NotBlank(message = "Description is required")
    @NotNull(message = "Description cannot be null")
    private String description;
    
    @PrePersist
	protected void onCreate() {
    	super.onCreate();
    	
    	if (rolePermissions == null) {
    		rolePermissions = new ArrayList<>();
    	}
    	
    	if (userRoles == null) {
			userRoles = new ArrayList<>();
    	}
	}

    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (RolePermission permissionRole : rolePermissions) {
            authorities.add(new SimpleGrantedAuthority(permissionRole.getPermission().getName()));
        }
        
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name));
        return authorities;
    }

    
    @Override
    public String toString() {
        return "Role{id=" + getId() + 
                ", name='" + name + '\'' + 
                ", description='" + description + '\'' + 
                '}';
    }
}