package com.example.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media")
public class Media {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private String publicId;

    @Column(name = "secure_url", nullable = false, unique = true, updatable = false)
    private String secureUrl;
    
    @Column(name = "file_type")
    private String fileType;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    // One-to-One relationship with MediaPost (optional back reference)
    @OneToOne(mappedBy = "media")
    @ToString.Exclude
    private MediaPost mediaPost;
    
    @PrePersist
    protected void onCreate() {
        createdDate = ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime();
    }
}