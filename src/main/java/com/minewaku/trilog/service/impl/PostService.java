package com.minewaku.trilog.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.minewaku.trilog.dto.Comment.CommentDTO;
import com.minewaku.trilog.dto.Comment.SavedCommentDTO;
import com.minewaku.trilog.dto.Comment.UpdatedCommentDTO;
import com.minewaku.trilog.dto.Media.SavedMediaPostDTO;
import com.minewaku.trilog.dto.Post.PostDTO;
import com.minewaku.trilog.dto.common.response.CloudinaryResponse;
import com.minewaku.trilog.dto.common.response.StatusResponse;
import com.minewaku.trilog.entity.Comment;
import com.minewaku.trilog.entity.MediaPost;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.entity.Post_;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.mapper.CommentMapper;
import com.minewaku.trilog.mapper.MediaMapper;
import com.minewaku.trilog.mapper.PostMapper;
import com.minewaku.trilog.repository.CommentRepository;
import com.minewaku.trilog.repository.PostRepository;
import com.minewaku.trilog.repository.UserRepository;
import com.minewaku.trilog.service.IPostService;
import com.minewaku.trilog.specification.SpecificationBuilder;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.FileUploadUtil;
import com.minewaku.trilog.util.MessageUtil;
import com.minewaku.trilog.util.SecurityUtil;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.metamodel.SingularAttribute;

@Service
public class PostService implements IPostService {
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private MediaPostService mediaPostService;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired	
	private UserRepository userRepository;

	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private MediaMapper mediaMapper;
	
	@Autowired
	private CommentMapper commentMapper;
	
    @Autowired
    private SpecificationBuilder<Post> specBuilder;

    @Autowired
    private ErrorUtil errorUtil;
    
    private Set<SingularAttribute<Post, ?> > allowedFieldsForFetch = new HashSet<>();

    @PostConstruct
    public void init() {
        @SuppressWarnings("unchecked")
        SingularAttribute<Post, ?> createdDate = (SingularAttribute<Post, ?>) (SingularAttribute<?, ?>) Post_.createdDate;
        allowedFieldsForFetch.add(createdDate);
    }
    
	public PostDTO findById(int id) {
		try {
			Post post = postRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND));
			return postMapper.entityToDto(post);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}	
	
	public Page<PostDTO> findAll(Map<String, String> params, Pageable pageable) {
		try {
        	Specification<Post> spec = specBuilder.buildSpecification(params, allowedFieldsForFetch);
            Page<Post> posts = postRepository.findAll(spec, pageable);
            Page<PostDTO> userDTOs = posts.map(post -> postMapper.entityToDto(post));
            return userDTOs;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Override
	public PostDTO create(PostDTO post) {
	 try {
		 	User user = userRepository.findById(post.getUserId()).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.USER_NOT_FOUND)); 
		 	Post entity = postMapper.dtoToEntity(post);
		 	entity.setUser(user);
           
            Post savedPost = postRepository.save(entity); 
            return postMapper.entityToDto(savedPost);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
	}
	
	@Override
    public PostDTO update(int id, PostDTO Post) {
        try {
            postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageUtil.getMessage("error.get.post"))); 
            Post savedPost = postRepository.save(postMapper.dtoToEntity(Post)); 
            return postMapper.entityToDto(savedPost);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(List<Integer> ids) {
        try {
            postRepository.softDeletePosts(ids); 
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
	
    @Transactional
    @Override
    public StatusResponse addMedia(Integer id, List<MultipartFile> files) {
    	try {
			Post updatedPost = postRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.POST_NOT_FOUND));
			for (MultipartFile file : files) {
				if (file != null && !file.isEmpty()) {
					FileUploadUtil.assertMediaAllowed(file);
					CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadFile(file, "/trilog/posts/images");
					SavedMediaPostDTO savedMediaPostDto = SavedMediaPostDTO.builder()
							.postId(updatedPost.getId())
							.publicId(cloudinaryResponse.getPublicId())
							.secureUrl(cloudinaryResponse.getSecureUrl())
							.build();
					MediaPost mediaPost = mediaMapper.mediaPostDtoToMediaPostEntity(mediaPostService.save(savedMediaPostDto));
	
					updatedPost.getMedia().add(mediaPost);
				}
			}
			
	    	int order = 0;
			for(MediaPost mediaPost : updatedPost.getMedia()) {
				mediaPost.setDisplayOrder(order++);
			}
			postRepository.save(updatedPost);
			return new StatusResponse(MessageUtil.getMessage("success.create"), ZonedDateTime.now(ZoneId.of("Z")));
    	} catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Transactional
    @Override
    public StatusResponse deleteMedia(Integer postId, List<Integer> mediaIds) {
    	try {
			Post updatedPost = postRepository.findById(postId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.POST_NOT_FOUND));
			mediaPostService.delete(mediaIds);
			
	    	int order = 0;
	    	Post newPost = postRepository.findById(postId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.POST_NOT_FOUND));
			for(MediaPost mediaPost : newPost.getMedia()) {
				mediaPost.setDisplayOrder(order++);
			}
			postRepository.save(updatedPost);
			return new StatusResponse(MessageUtil.getMessage("success.create"), ZonedDateTime.now(ZoneId.of("Z")));
    	} catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Transactional
    public CommentDTO addComment(Integer id, SavedCommentDTO comment) {
    	try {
	    	Post post = postRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.POST_NOT_FOUND));
	    	User owner = (User) SecurityUtil.getPrincipal();
	    	
			Comment savedComment = Comment.builder()
					.post(post)
					.user(owner)
					.content(comment.getContent())
					.build();
			
			commentRepository.save(savedComment);
			return commentMapper.entityToDto(savedComment);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }
    
    @Transactional
    public CommentDTO addReplyComment(Integer commentId, SavedCommentDTO comment) {
    	try {
    	   	Comment parentComment = commentRepository.findById(commentId).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.COMMENT_NOT_FOUND));
        	User owner = (User) SecurityUtil.getPrincipal();
        	
    		Comment savedComment = Comment.builder()
    				.replyTo(parentComment)
    				.post(parentComment.getPost())
    				.user(owner)
    				.content(comment.getContent())
    				.build();
    		
    		commentRepository.save(savedComment);
    		return commentMapper.entityToDto(savedComment);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage(), e);
    	}
 
    }	
    
    @Transactional
    public CommentDTO updateComment(Integer id, UpdatedCommentDTO comment) {
    	try {
	    	Comment updatedComment = commentRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.COMMENT_NOT_FOUND));
	    	updatedComment.setContent(comment.getContent());
			
			commentRepository.save(updatedComment);
			return commentMapper.entityToDto(updatedComment);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
    }
    
    @Transactional
    public StatusResponse deleteComment(List<Integer> ids) {
	    try {
	    	commentRepository.deleteAllById(ids);
			return new StatusResponse(MessageUtil.getMessage("success.create"), ZonedDateTime.now(ZoneId.of("Z")));	
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
    }
}
