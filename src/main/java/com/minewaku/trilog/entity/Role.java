package com.minewaku.trilog.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_permission",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

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
    	permissions = new HashSet<>();
		isDeleted = false;
	}

    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Permission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE" + this.name));
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