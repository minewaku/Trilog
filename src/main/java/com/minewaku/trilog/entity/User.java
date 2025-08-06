package com.minewaku.trilog.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
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
@Table(name = "user")
@DynamicUpdate
@SuperBuilder
public class User extends BaseEntity implements UserDetails, CredentialsContainer {
	
//	@JsonManagedReference
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<UserRole> userRoles;
	
	@Column(name = "email", nullable = false, unique = true)
	@Email(message = "Invalid email")
	@NotBlank(message = "Email is required")
	@NotNull(message = "Email cannot be null")
	private String email;
	
	@Column(name = "hashed_password", nullable = false)
	@NotBlank(message = "Password is required")
	@NotNull(message = "Password cannot be null")
	private String hashed_password;
	
	@Column(name = "name", length = 255, nullable = false, unique = true)
	@NotBlank(message = "Name is required")
	@NotNull(message = "Name cannot be null")
	private String name;
	
	@Column(name = "bio", length = 500, nullable = true)
	private String bio;
	
	@Column(name = "birthdate")
	private LocalDate birthdate;

	@Column(name = "phone", length = 20)
//	@Phone(message = "Invalid phone number")
	private String phone;
	
	@Column(name = "address")
	private String address;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Media image;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cover_id", referencedColumnName = "id")
    private Media cover;
	
	
	//banned status would be separated into a dependent table, it will be added later. would contain banned reason, banned by, banned date, etc.

	//The account is online or not
	@Column(name = "is_active", nullable = false)
	@NotNull(message = "Active is required")
	private Boolean isActive;
	
	//The current account is enabled to login or not (not verified the email yet) (unable to login if false) (its previous name is is_verified and only be used for email confirmation)
	@Column(name = "is_enabled", nullable = false)
	@NotNull(message = "Enabled is required")
	private Boolean isEnabled;
	
	//The current account is locked or not due to suspicious login attempt, banned by admin... (unable to login if true). The main purpose of this field is to prevent user to login
	//There is another Role called BANNED would allow users to login but totally prevent them to do any action on the server
	@Column(name = "is_locked", nullable = false)
	@NotNull(message = "Locked is required")
	private Boolean isLocked;
	
	//Soft deleted (unable to login if true, pretend that the account is not exist anymore)
	@Column(name = "is_deleted", nullable = false)
	@NotNull(message = "Deleted is required")
	private Boolean isDeleted;
	
	
	@PrePersist
	protected void onCreate() {
		super.onCreate();
		
		if (userRoles == null) {
			userRoles = new ArrayList<>();
		}
		
		if (isActive == null) {
			isActive = true;
		}
		
		if (isEnabled == null) {
			isEnabled = false;
		}
		
		if (isLocked == null) {			
			isLocked = false;
		}
		
		if( isDeleted == null) {
			isDeleted = false;			
		}
	}
	
	public Set<Role> getRoles() {
//	    if (userRoles == null) return Set.of();
	    return userRoles.stream()
	        .map(UserRole::getRole)
	        .collect(Collectors.toSet());
	}

	
	@Override
	public String getUsername() {
		return email;
	}

	@Override
	@Transactional
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return userRoles.stream()
	                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName()))
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

	//Deprecated (Using the checking mechanism of this system instead)
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
//		this.hashed_password = null;
	}

}
