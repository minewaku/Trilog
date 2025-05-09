package com.minewaku.trilog.api.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minewaku.trilog.dto.PostDTO;
import com.minewaku.trilog.dto.UserDTO;
import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.dto.response.CursorPage;
import com.minewaku.trilog.search.service.impl.ESPostService;
import com.minewaku.trilog.search.service.impl.ESUserService;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
	
	@Autowired
	private ESPostService esPostService;
	
	@Autowired
	private ESUserService esUserService;
	
	//Post search
	@GetMapping("/posts")
	public ResponseEntity<CursorPage<PostDTO>> search(@RequestParam("q") String keyword, @RequestBody Cursor cursor) {
		return ResponseEntity.status(HttpStatus.OK).body(esPostService.searchByContent(keyword, cursor));
	}

	@GetMapping("/posts/hashtag")
	public ResponseEntity<CursorPage<PostDTO>> searchHashtag(@RequestParam("q") String hashtag, @RequestBody Cursor cursor) {
		return ResponseEntity.status(HttpStatus.OK).body(esPostService.searchByHashtag(hashtag, cursor));
	}
	
	
	//User search
	@GetMapping("/suggest/users")
	public ResponseEntity<List<UserDTO>> searchUser(@RequestParam("q") String username, @RequestBody Cursor cursor) {
		return ResponseEntity.status(HttpStatus.OK).body(esUserService.suggestUsers(username));
	}
}
