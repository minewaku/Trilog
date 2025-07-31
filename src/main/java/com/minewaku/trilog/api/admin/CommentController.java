package com.minewaku.trilog.api.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping(path = "/{id}")
	public ResponseEntity<Void> addRepyComment(@PathVariable Integer id, SavedCommentDTO savedCommentDTO) {
		postService.addReplyComment(id, savedCommentDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<Void> updateComment(@PathVariable Integer id, UpdatedCommentDTO updatedCommentDTO) {
		postService.updateComment(id, updatedCommentDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@DeleteMapping(path = "/{ids}")
	public ResponseEntity<Void> deleteComment(@PathVariable Integer id, String ids) {
		List<Integer> idList = DataPreprocessingUtil.parseCommaSeparatedIds(ids);
		postService.deleteComment(idList);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
