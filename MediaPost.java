package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media_post")
public class MediaPost {
    
    @Id
    private Integer id;
    
    // One-to-One relationship with Media using @MapsId
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", referencedColumnName = "id")
    @ToString.Exclude
    private Media media;
    
    // Many-to-One relationship with Post
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post;
    
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;
    
    @Column(name = "is_thumbnail", nullable = false)
    private Boolean isThumbnail = false;
    
    @Column(name = "caption")
    private String caption;
    
    @PrePersist
    protected void onCreate() {
        updateThumbnailStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        updateThumbnailStatus();
    }
    
    private void updateThumbnailStatus() {
        if (displayOrder != null && displayOrder <= 3) {
            isThumbnail = true;
        } else {
            isThumbnail = false;
        }
    }
}