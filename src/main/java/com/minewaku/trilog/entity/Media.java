package com.minewaku.trilog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "media")
@SuperBuilder
public class Media {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

    @Column(name = "public_id", unique = true)
    @NotBlank(message = "publicId is required")
    private String publicId;

    @Column(name = "secure_url", unique = true)
    @NotBlank(message = "secureUrl is required")
    private String secureUrl;
}

