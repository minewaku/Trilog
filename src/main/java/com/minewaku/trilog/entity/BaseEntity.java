package com.minewaku.trilog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@MappedSuperclass
public abstract class BaseEntity implements Serializable{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @Column(name = "modifiedDate")
    private LocalDateTime modifiedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime();
    }
}
