package com.minewaku.trilog.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
@Table(name = "post")
@DynamicUpdate
@SuperBuilder
public class Post extends BaseEntity {

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<MediaPost> media;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;	
    
    @Column(name = "like_count", nullable = false)
    @NotNull(message = "Like count cannot be null")
    @Min(0)
    @Check(constraints = "like_count >= 0")
    private Integer likeCount;

    @Column(name = "view_count", nullable = false)
    @NotNull(message = "View count cannot be null")
    @Min(0)
    @Check(constraints = "view_count >= 0")
    private Integer viewCount;

    @Column(name = "comment_count", nullable = false)
    @NotNull(message = "Comment count cannot be null")
    @Min(0)
    @Check(constraints = "comment_count >= 0")
    private Integer commentCount;
    
    @Column(name = "status", nullable = false)
    @NotNull(message = "Status cannot be null")
    @Check(constraints = "status IN (0, 1, 2, 3)")
    private Integer status;
    
    @Column(name = "lat")
    private Double lat;
    
    @Column(name = "lon")
    private Double lon;
    
    @PrePersist
	protected void onCreate() {
		super.onCreate();
		
		if(status == null)
			status = 0;
		
		if (media == null)
			media = new HashSet<>();
		
		likeCount = 0;
		viewCount = 0;
		commentCount = 0;
    }
}