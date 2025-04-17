package com.minewaku.trilog.api.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.dto.request.RegisterRequest;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.search.service.impl.PostIdListService;
import com.minewaku.trilog.search.service.impl.PostService;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
	@Autowired
    private PostIdListService postIdListService;
	
	@Autowired
	private PostService postService;
	
	@GetMapping("/feed")
	public void register(@RequestBody RegisterRequest request) {
//        postIdListService.fetchFeed();
    }
	
	@GetMapping("/search")
	public void search(@PathVariable String keyword, @RequestBody Cursor cursor) {
		List<Post> result = postService.searchByContent(keyword, cursor);
		return ResponseEntity.status(HttpStatus.OK)
                 .body(result); 
	
	
}
