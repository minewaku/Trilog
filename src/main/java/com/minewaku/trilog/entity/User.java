package com.minewaku.trilog.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "user")
@DynamicUpdate
@SuperBuilder
public class User extends BaseEntity implements UserDetails, CredentialsContainer {
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "user_role",
	    joinColumns = @JoinColumn(name = "user_id"),
	    inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;

	@Column(name = "email", unique = true)
	@Email(message = "Invalid email")
	@NotBlank(message = "Email is required")
	private String email;
	
	@Column(name = "hashed_password")
	@NotBlank(message = "Password is required")
	private String hashed_password;
	
	@Column(name = "name", length = 255, unique = true)
	@NotBlank(message = "Name is required")
	private String name;
	
	@Column(name = "birthdate", nullable = true)
	private LocalDate birthdate;

	@Column(name = "phone", length = 20, nullable = true)
	private String phone;
	
	//required for this field later
	@Column(name = "address", nullable = true)
	private String address;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Media image;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cover_id", referencedColumnName = "id")
    private Media cover;
	
	
	//banned status would be separated into a dependent table, it will be added later. would contain banned reason, banned by, banned date, etc.

	//The account is online or not
	@Column(name = "is_active")
	@NotNull(message = "Active is required")
	private Boolean isActive;
	
	//The current account is enabled to login or not (not verified the email yet) (unable to login if false) (its previous name is is_verified and only be used for email confirmation)
	@Column(name = "is_enabled")
	@NotNull(message = "Enabled is required")
	private Boolean isEnabled;
	
	//The current account is locked or not due to suspicious login attempt, banned by admin... (unable to login if true)
	@Column(name = "is_locked")
	@NotNull(message = "Locked is required")
	private Boolean isLocked;
	
	//Soft deleted (unable to login if true)
	@Column(name = "is_deleted")
	@NotNull(message = "Deleted is required")
	private Boolean isDeleted;
	
	
	@PrePersist
	protected void onCreate() {
		super.onCreate();
		
		//avoiding override current value
		if (roles == null) {			
			roles = new HashSet<>();
		}
		
		if (address == null) {
			address = "";
		}
		
		if (isActive == null) {
			isActive = true;
		}
		
		if (isEnabled == null) {
			isEnabled = false;
		}

		isLocked = false;
		isDeleted = false;
	}
	
	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return roles.stream()
	                .map(role -> new SimpleGrantedAuthority(role.getName()))
	                .collect(Collectors.toSet());
	}


	@Override
	public String getPassword() {
		return hashed_password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//Deprecated (Used the checking mechanism of this system instead)
	@Override
	public boolean isAccountNonLocked() {
//		return isEnabled;
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//Deprecated (Used the checking mechanism of this system instead)
	@Override
	public boolean isEnabled() {
//		return isEnabled && !isDeleted;
		return true;
	}

	@Override
	public void eraseCredentials() {
		this.hashed_password = null;
	}

}
