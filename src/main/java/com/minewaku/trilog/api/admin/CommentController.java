package com.minewaku.trilog.api.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minewaku.trilog.dto.Comment.CommentDTO;
import com.minewaku.trilog.dto.Comment.SavedCommentDTO;
import com.minewaku.trilog.dto.Comment.UpdatedCommentDTO;
import com.minewaku.trilog.service.impl.CommentService;
import com.minewaku.trilog.service.impl.PostService;
import com.minewaku.trilog.util.DataPreprocessingUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Comment", description = "Comment API")
@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
	/**
	 * 
	 * @summary POST /api/v1/comments/{id} - @see {@link #addReplyComment}
	 * @summary PUT /api/v1/comments/{id} - @see {@link #updateComment}
	 * @summary DELETE /api/v1/comments/{ids} - @see {@link #deleteComment}
	 * 
	 */
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping(path = "/{id}/comments")
	public ResponseEntity<List<CommentDTO>> getReplyComment(@PathVariable Integer id, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.findReplyComments(id, pageable));
	}
    
	
	@PostMapping(path = "/{id}")
	public ResponseEntity<CommentDTO> addRepyComment(@PathVariable Integer id, @RequestBody SavedCommentDTO savedCommentDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(postService.addReplyComment(id, savedCommentDTO));
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<CommentDTO> updateComment(@PathVariable Integer id, @RequestBody UpdatedCommentDTO updatedCommentDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(postService.updateComment(id, updatedCommentDTO));
	}
	
	@DeleteMapping(path = "/{ids}")
	public ResponseEntity<Void> deleteComment(@PathVariable String ids) {
		List<Integer> idList = DataPreprocessingUtil.parseCommaSeparatedIds(ids);
		postService.deleteComment(idList);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
