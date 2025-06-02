package com.minewaku.trilog.api.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minewaku.trilog.dto.LikeDTO;
import com.minewaku.trilog.service.impl.LikeService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Like", description = "Like API")
@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {
	
	@Autowired
	private LikeService likeService;
	
	/**
	 * ALL AVAILABLE APIS
	 * 
	 * @summary GET /api/v1/likes - @see #findAll
	 * 
	 */
	
	@GetMapping("")
	public ResponseEntity<Page<LikeDTO>> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(likeService.findAll(pageable, params));
	}
	
}
