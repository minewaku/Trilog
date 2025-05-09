package com.minewaku.trilog.entity;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "confirmation_token")
@SuperBuilder
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token")
	@NotNull(message = "token is required")
    private String token;

	@NotNull(message = "createdDate is required")
    private LocalDateTime createdDate;

	@NotNull(message = "expiredDate is required")
    private LocalDateTime expiredDate;

    private LocalDateTime confirmedDate;
    
    @ManyToOne
    @NotNull(message = "user is required")
    @JoinColumn(name = "user_id")
    private User user;
    
    @PrePersist
    protected void onCreate() {
        createdDate = ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime();
    }
}