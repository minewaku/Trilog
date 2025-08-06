package com.example.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    // One-to-Many relationship with MediaPost
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<MediaPost> mediaPosts = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;	
    
    @Column(name = "like_count", nullable = false)
    @NotNull(message = "Like count cannot be null")
    @Min(0)
    private Integer likeCount = 0;

    @Column(name = "view_count", nullable = false)
    @NotNull(message = "View count cannot be null")
    @Min(0)
    private Integer viewCount = 0;

    @Column(name = "comment_count", nullable = false)
    @NotNull(message = "Comment count cannot be null")
    @Min(0)
    private Integer commentCount = 0;
    
    @Column(name = "status", nullable = false)
    @NotNull(message = "Status cannot be null")
    private Integer status = 0; // 0=draft, 1=published, 2=archived, 3=deleted
    
    @Column(name = "lat")
    private Double latitude;
    
    @Column(name = "lon")
    private Double longitude;
    
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @PrePersist
    protected void onCreate() {
        createdDate = ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime();
        updatedDate = createdDate;
        
        if (status == null) {
            status = 0;
        }
        
        if (mediaPosts == null) {
            mediaPosts = new ArrayList<>();
        }
    }
    
    // Utility method to add MediaPost
    public void addMediaPost(MediaPost mediaPost) {
        mediaPosts.add(mediaPost);
        mediaPost.setPost(this);
    }
    
    // Utility method to remove MediaPost
    public void removeMediaPost(MediaPost mediaPost) {
        mediaPosts.remove(mediaPost);
        mediaPost.setPost(null);
    }
}