package com.minewaku.trilog.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minewaku.trilog.dto.PostDTO;
import com.minewaku.trilog.service.IPostService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post", description = "Post API")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
	
	@Autowired
	private IPostService postService;
	
	@PostMapping("")
	public ResponseEntity<PostDTO> add(@RequestBody PostDTO post) {
		return ResponseEntity.status(HttpStatus.OK).body(postService.create(post));
	}
}
