package com.minewaku.trilog.entity;

import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Media> media;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "likes", nullable = false)
    private Integer likes;
    
    @Column(name = "views", nullable = false)
    private Integer views;
    
    @Column(name = "comments", nullable = false)
    private Integer comments;
    
    @Column(name = "status", nullable = false)
    private Integer status;
    
    @Column(name = "lat")
    private Double locationLat;
    
    @Column(name = "lon")
    private Double locationLon;
    
    @PrePersist
	protected void onCreate() {
		super.onCreate();
		
		if(status == null)
			status = 0;
		
		if (media == null)
			media = new HashSet<>();
		
		likes = 0;
		views = 0;
		comments = 0;
    }
}