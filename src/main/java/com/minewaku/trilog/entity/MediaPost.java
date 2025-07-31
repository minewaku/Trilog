package com.minewaku.trilog.entity;

import org.hibernate.annotations.DynamicUpdate;

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
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "media_post")
@DynamicUpdate
@SuperBuilder
public class MediaPost {
	
    @Id
    private Integer id;
	
    @MapsId
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id", referencedColumnName = "id")
	private Media media;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;
	
	@Column(name = "display_order", nullable = false)
	private Integer displayOrder;
	
	@Column(name = "is_thumbnail", nullable = false)
	private Boolean isThumbnail;
	
    @PrePersist
    protected void onCreate() {
        if(displayOrder <= 3) {
        	isThumbnail = true;
        } else {
        	isThumbnail = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if(displayOrder <= 3) {
        	isThumbnail = true;
        } else {
        	isThumbnail = false;
        }
    }
}
