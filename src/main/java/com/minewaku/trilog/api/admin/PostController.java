package com.minewaku.trilog.api.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minewaku.trilog.dto.PostDTO;
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
	 * 
	 * @summary POST /api/v1/posts - @see {@link #add}
	 * @summary PUT /api/v1/posts/{id} - @see {@link #update}
	 * @summary DELETE /api/v1/posts/{ids} - @see {@link #delete}
	 * 
	 */
	
	@Autowired
	private IPostService postService;
	
	@Autowired
	private ILikeService likeService;	
	
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
	
	@PostMapping("")
	public ResponseEntity<PostDTO> add(@RequestBody PostDTO post) {
		return ResponseEntity.status(HttpStatus.OK).body(postService.create(post));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PostDTO> update(@PathVariable int id, @RequestBody PostDTO post) {
		return ResponseEntity.status(HttpStatus.OK).body(postService.update(id, post));
	}
	
	@DeleteMapping("/{ids}")
	public ResponseEntity<Void> delete(@PathVariable String ids) {
		List<Integer> idList = DataPreprocessingUtil.parseCommaSeparatedIds(ids);
		postService.delete(idList);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
