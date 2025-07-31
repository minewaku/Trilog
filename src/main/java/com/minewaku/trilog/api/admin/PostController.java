package com.minewaku.trilog.api.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.minewaku.trilog.dto.Comment.CommentDTO;
import com.minewaku.trilog.dto.Comment.SavedCommentDTO;
import com.minewaku.trilog.dto.Media.MediaPostDTO;
import com.minewaku.trilog.dto.Post.PostDTO;
import com.minewaku.trilog.dto.Post.SavedPostDTO;
import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.facade.UploadPostFacade;
import com.minewaku.trilog.search.service.ESIPostService;
import com.minewaku.trilog.service.ILikeService;
import com.minewaku.trilog.service.IPostService;
import com.minewaku.trilog.util.DataPreprocessingUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post", description = "Post API")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
	
	/**
	 * ALL AVAILABLE APIS
	 * 
	 * @summary GET /api/v1/posts/{id}/likes - @see {@link #like}
	 * @summary DELETE /api/v1/posts/{id}/likes - @see {@link #unlike}
	 *
	 * 
	 * @summary GET /api/v1/posts/{id} - @see {@link #findById}
	 * @summary GET /api/v1/posts - @see {@link #findAll}
	 * 
	 * 
	 * @summary POST /api/v1/posts/search - @see {@link #searchByContent}
	 * @summary POST /api/v1/posts/hashtag/search - @see {@link #searchByHashtag}
	 * 
	 * 
	 * @summary POST /api/v1/posts - @see {@link #add}
	 * @summary PUT /api/v1/posts/{id} - @see {@link #update}
	 * @summary DELETE /api/v1/posts/{ids} - @see {@link #delete}
	 * 
	 * 
	 * @summary POST /api/v1/posts/{id}/media - @see {@link #addMedia}
	 * @summary DELETE /api/v1/posts/{postId}/media/{mediaIds} - @see {@link #deleteMedia}
	 *
	 *
	 * @summary POST /api/v1/posts/{id}/comments - @see {@link #addComment}
	 * 
	 */
	
	@Autowired
	private UploadPostFacade uploadPostFacade;
	
	@Autowired
	private IPostService postService;
	
	@Autowired
	private ILikeService likeService;	
	
    @Autowired
    private ESIPostService esPostService;

	
	@PostMapping("/{id}/likes")
	public ResponseEntity<Void> like(@PathVariable int id) {
		likeService.cachedLikePost(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@DeleteMapping("/{id}/likes")
	public ResponseEntity<Void> unlike(@PathVariable int id) {
		likeService.uncachedLikePost(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostDTO> findById(@PathVariable int id) {
		PostDTO post = postService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(post);
	}
	
	@GetMapping(path = "")
	public ResponseEntity<Page<PostDTO>> findAll(Map<String, String> params, Pageable pageable) {
		Page<PostDTO> posts = postService.findAll(params, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(posts);
	}
	
	@GetMapping(path = "/search")
	public ResponseEntity<CursorPage<PostDTO>> searchByContent(@PathVariable String q, @RequestBody Cursor cursor) {
		CursorPage<PostDTO> posts = esPostService.searchByContent(q, cursor);
		return ResponseEntity.status(HttpStatus.OK).body(posts);
	}
	
	@GetMapping(path = "/hashtag/search")
	public ResponseEntity<CursorPage<PostDTO>> searchByHashtag(@PathVariable String q, @RequestBody Cursor cursor) {
		CursorPage<PostDTO> posts = esPostService.searchByHashtag(q, cursor);
		return ResponseEntity.status(HttpStatus.OK).body(posts);
	}
	
	@PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PostDTO> add(@RequestBody SavedPostDTO post,  @RequestPart("files") List<MultipartFile> files) {
		return ResponseEntity.status(HttpStatus.OK).body(uploadPostFacade.uploadPost(post, files));
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<PostDTO> update(@PathVariable int id, @RequestBody PostDTO post, @RequestBody List<MediaPostDTO> media) {
		return ResponseEntity.status(HttpStatus.OK).body(postService.update(id, post));
	}
	
	@DeleteMapping("/{ids}")
	public ResponseEntity<Void> delete(@PathVariable String ids) {
		List<Integer> idList = DataPreprocessingUtil.parseCommaSeparatedIds(ids);
		postService.delete(idList);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping(path = "/{id}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> addMedia(@PathVariable int id,  @RequestPart("files") List<MultipartFile> files) {
		postService.addMedia(id, files);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping(path = "/{postId}/media/{mediaIds}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> deleteMedia(@PathVariable int postId, @PathVariable String mediaIds) {
		List<Integer> idList = DataPreprocessingUtil.parseCommaSeparatedIds(mediaIds);
		postService.deleteMedia(postId, idList);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping(path = "/{id}/comments")
	public ResponseEntity<CommentDTO> addComment(@PathVariable int id, SavedCommentDTO savedCommentDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(postService.addComment(id, savedCommentDTO));
	}
}
