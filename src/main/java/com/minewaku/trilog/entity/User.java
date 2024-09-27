package com.minewaku.trilog.entity;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@SuperBuilder
public class User extends BaseEntity implements UserDetails, CredentialsContainer {
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

	@Column(name = "email")
	@Email(message = "Invalid email")
	@NotBlank(message = "Email is required")
	private String email;
	
	@Column(name = "hashed_password")
	@NotBlank(message = "Password is required")
	private String hashed_password;
	
	@Column(name = "name", length = 255, unique = true)
	@NotBlank(message = "Name is required")
	private String name;
	
	@Column(name = "birthdate")
	@NotNull(message = "Birthdate is required")
	private LocalDate birthdate;

	@Column(name = "phone", length = 20)
	@NotBlank(message = "Phone is required")
	private String phone;
	
	@Column(name = "address")
	@NotBlank(message = "Address is required")
	private String address;

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Media image;

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cover_id", referencedColumnName = "id")
    private Media cover;

	@Column(name = "active")
	@NotNull(message = "Active is required")
	final private Boolean active = true;

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role.getAuthorities();
	}

	@Override
	public String getPassword() {
		return hashed_password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}

	@Override
	public void eraseCredentials() {
		this.hashed_password = null;
	}

}
