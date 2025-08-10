package com.minewaku.trilog.entity;

import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "comment")
@DynamicUpdate
@SuperBuilder
public class Comment extends BaseEntity {
	
    // Self-referencing ManyToOne (owning side)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_id", nullable = true)
    private Comment replyTo;

    // Self-referencing OneToMany (inverse side)
    @OneToMany(mappedBy = "replyTo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments;
	
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @Column(name = "content", nullable = false, length = 500)
    private String content;
}
