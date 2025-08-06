package com.minewaku.trilog.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.minewaku.trilog.entity.EmbededId.UserRoleId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "user_role")
@Builder
@DynamicUpdate
public class UserRole {
	
    @EmbeddedId
    private UserRoleId id;
    
//    @JsonBackReference
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

//    @JsonBackReference
    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false, updatable = false)
    private Role role;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_id", nullable = false, updatable = false)
    private User createdBy;
    
    @Column(name = "created_date", nullable = false, updatable = false)
    @NotNull(message = "Created date cannot be null")
    private LocalDateTime createdDate;
    
    @PrePersist
    protected void onCreate() {
    	if (id == null) {
			id = new UserRoleId();
		}
    	
    	if (createdBy == null) {
    		User systemUser = new User();
    		systemUser.setId(1);
    		createdBy = systemUser;
    	}
    	
        createdDate = ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime();
    }
}
