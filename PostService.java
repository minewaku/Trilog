package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Media;
import com.example.entity.MediaPost;
import com.example.entity.Post;
import com.example.entity.User;
import com.example.repository.MediaPostRepository;
import com.example.repository.MediaRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private MediaRepository mediaRepository;
    
    @Autowired
    private MediaPostRepository mediaPostRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a post with media - CORRECT WAY to handle @MapsId relationships
     */
    public Post createPostWithMedia(String content, Integer userId, List<CreateMediaRequest> mediaRequests) {
        // 1. Get the user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // 2. Create and save Media entities FIRST
        List<Media> savedMediaList = new ArrayList<>();
        for (CreateMediaRequest mediaRequest : mediaRequests) {
            Media media = new Media();
            media.setPublicId(mediaRequest.getPublicId());
            media.setSecureUrl(mediaRequest.getSecureUrl());
            media.setFileType(mediaRequest.getFileType());
            media.setFileSize(mediaRequest.getFileSize());
            
            // Save Media first to get the generated ID
            Media savedMedia = mediaRepository.save(media);
            savedMediaList.add(savedMedia);
        }
        
        // 3. Create Post
        Post post = new Post();
        post.setContent(content);
        post.setUser(user);
        post.setStatus(1); // Published
        
        // Save Post to get the generated ID
        Post savedPost = postRepository.save(post);
        
        // 4. Create MediaPost entities linking Media and Post
        List<MediaPost> mediaPosts = new ArrayList<>();
        for (int i = 0; i < savedMediaList.size(); i++) {
            Media savedMedia = savedMediaList.get(i);
            CreateMediaRequest mediaRequest = mediaRequests.get(i);
            
            MediaPost mediaPost = new MediaPost();
            // IMPORTANT: Set the ID explicitly for @MapsId
            mediaPost.setId(savedMedia.getId());
            mediaPost.setMedia(savedMedia);
            mediaPost.setPost(savedPost);
            mediaPost.setDisplayOrder(i + 1);
            mediaPost.setCaption(mediaRequest.getCaption());
            
            // Save MediaPost
            MediaPost savedMediaPost = mediaPostRepository.save(mediaPost);
            mediaPosts.add(savedMediaPost);
        }
        
        // 5. Update Post's collection (optional, for return value completeness)
        savedPost.setMediaPosts(mediaPosts);
        
        return savedPost;
    }

    /**
     * Alternative approach - Save from Post side with proper setup
     */
    public Post createPostWithMediaCascade(String content, Integer userId, List<CreateMediaRequest> mediaRequests) {
        // 1. Get the user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // 2. Create Post
        Post post = new Post();
        post.setContent(content);
        post.setUser(user);
        post.setStatus(1);
        
        // 3. Create Media and MediaPost entities
        List<MediaPost> mediaPosts = new ArrayList<>();
        for (int i = 0; i < mediaRequests.size(); i++) {
            CreateMediaRequest mediaRequest = mediaRequests.get(i);
            
            // Create and save Media first
            Media media = new Media();
            media.setPublicId(mediaRequest.getPublicId());
            media.setSecureUrl(mediaRequest.getSecureUrl());
            media.setFileType(mediaRequest.getFileType());
            media.setFileSize(mediaRequest.getFileSize());
            Media savedMedia = mediaRepository.save(media);
            
            // Create MediaPost
            MediaPost mediaPost = new MediaPost();
            mediaPost.setId(savedMedia.getId()); // Set ID for @MapsId
            mediaPost.setMedia(savedMedia);
            mediaPost.setDisplayOrder(i + 1);
            mediaPost.setCaption(mediaRequest.getCaption());
            
            // Set up bidirectional relationship
            mediaPost.setPost(post);
            mediaPosts.add(mediaPost);
        }
        
        // 4. Set MediaPosts to Post and save (will cascade)
        post.setMediaPosts(mediaPosts);
        return postRepository.save(post);
    }

    /**
     * Add media to existing post
     */
    public Post addMediaToPost(Integer postId, CreateMediaRequest mediaRequest) {
        // 1. Get existing post
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        
        // 2. Create and save Media
        Media media = new Media();
        media.setPublicId(mediaRequest.getPublicId());
        media.setSecureUrl(mediaRequest.getSecureUrl());
        media.setFileType(mediaRequest.getFileType());
        media.setFileSize(mediaRequest.getFileSize());
        Media savedMedia = mediaRepository.save(media);
        
        // 3. Create MediaPost
        MediaPost mediaPost = new MediaPost();
        mediaPost.setId(savedMedia.getId());
        mediaPost.setMedia(savedMedia);
        mediaPost.setPost(post);
        
        // Set display order (next in sequence)
        int nextOrder = post.getMediaPosts().size() + 1;
        mediaPost.setDisplayOrder(nextOrder);
        mediaPost.setCaption(mediaRequest.getCaption());
        
        // 4. Save MediaPost and update Post
        mediaPostRepository.save(mediaPost);
        post.getMediaPosts().add(mediaPost);
        
        return post;
    }

    /**
     * Update post media order
     */
    public Post updateMediaOrder(Integer postId, List<MediaOrderRequest> orderRequests) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        
        for (MediaOrderRequest orderRequest : orderRequests) {
            MediaPost mediaPost = mediaPostRepository.findById(orderRequest.getMediaId())
                .orElseThrow(() -> new RuntimeException("MediaPost not found"));
            
            mediaPost.setDisplayOrder(orderRequest.getNewOrder());
            mediaPostRepository.save(mediaPost);
        }
        
        return postRepository.findById(postId).orElseThrow();
    }

    /**
     * Remove media from post
     */
    public Post removeMediaFromPost(Integer postId, Integer mediaId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        
        MediaPost mediaPost = mediaPostRepository.findById(mediaId)
            .orElseThrow(() -> new RuntimeException("MediaPost not found"));
        
        // Remove from Post's collection
        post.getMediaPosts().remove(mediaPost);
        
        // Delete MediaPost (this will orphan the Media)
        mediaPostRepository.delete(mediaPost);
        
        return post;
    }

    // DTO Classes
    public static class CreateMediaRequest {
        private String publicId;
        private String secureUrl;
        private String fileType;
        private Long fileSize;
        private String caption;
        
        // Constructors, getters, setters
        public CreateMediaRequest() {}
        
        public CreateMediaRequest(String publicId, String secureUrl, String fileType, Long fileSize, String caption) {
            this.publicId = publicId;
            this.secureUrl = secureUrl;
            this.fileType = fileType;
            this.fileSize = fileSize;
            this.caption = caption;
        }
        
        // Getters and setters
        public String getPublicId() { return publicId; }
        public void setPublicId(String publicId) { this.publicId = publicId; }
        public String getSecureUrl() { return secureUrl; }
        public void setSecureUrl(String secureUrl) { this.secureUrl = secureUrl; }
        public String getFileType() { return fileType; }
        public void setFileType(String fileType) { this.fileType = fileType; }
        public Long getFileSize() { return fileSize; }
        public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
        public String getCaption() { return caption; }
        public void setCaption(String caption) { this.caption = caption; }
    }
    
    public static class MediaOrderRequest {
        private Integer mediaId;
        private Integer newOrder;
        
        public MediaOrderRequest() {}
        
        public MediaOrderRequest(Integer mediaId, Integer newOrder) {
            this.mediaId = mediaId;
            this.newOrder = newOrder;
        }
        
        public Integer getMediaId() { return mediaId; }
        public void setMediaId(Integer mediaId) { this.mediaId = mediaId; }
        public Integer getNewOrder() { return newOrder; }
        public void setNewOrder(Integer newOrder) { this.newOrder = newOrder; }
    }
}