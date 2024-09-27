package com.minewaku.trilog.auth;

import java.time.LocalDate;

import com.minewaku.trilog.entity.Role;

public class RegisterRequest {

    private String name;
    private Role role;
    private String email;
    private String password;
    private LocalDate birthdate;
    private String phone;
    private String address;

    public RegisterRequest(String email, Role role, String name, String password, LocalDate birthdate, String phone, String address) {
        this.email = email;
        this.role = role;
        this.name = name;
        this.password = password;
        this.birthdate = birthdate;
		this.phone = phone;
		this.address = address;
    }

    public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthdate = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}


